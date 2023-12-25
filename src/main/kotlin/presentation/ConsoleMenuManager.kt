package presentation

import di.DI
import domain.FilmController
import domain.SessionController
import domain.TicketService
import kotlinx.datetime.LocalDateTime
import presentation.model.OutputModel
import kotlin.Exception

class ConsoleMenuManager(
    private val filmController: FilmController,
    private val sessionController: SessionController,
    private val ticketService: TicketService
) : MenuManager {
    override fun showOptions() {
        print("Available actions:\n\t")
        println(MenuOption.entries.mapIndexed { index, menuAction -> "${index + 1}. $menuAction" }.joinToString("\n\t"))
    }

    override fun getRequest(): MenuOption? {
        return readlnOrNull()?.let { parseAction(it) }
    }

    override fun processRequest(request: MenuOption?) {
        when (request) {
            MenuOption.SellTicket -> sellTicket()
            MenuOption.ReturnTicket -> returnTicket()
            MenuOption.ShowAvailableSeats -> showAvailableSeats()
            MenuOption.AddFilmInfo -> addFilmInfo()
            MenuOption.EditFilmName -> editFilmName()
            MenuOption.EditFilmDuration -> editFilmDuration()
            MenuOption.EditSessionStartTime -> editSessionStartTime()
            MenuOption.EditSessionFilm -> editFilmName()
            MenuOption.MarkOccupiedSeat -> markOccupiedSeat()
            MenuOption.CreateSession -> createSession()
            MenuOption.Exit -> return
            else -> println("Unhandled option...")
        }
    }

    override fun handleInteractions() {
        do {
            showOptions()
            val request = getRequest()
            processRequest(request)

        } while (request != MenuOption.Exit)
        println("Exiting...")
    }


    private fun parseAction(userInput: String): MenuOption? {
        return try {
            if (userInput[0].isDigit()) {
                MenuOption.entries[userInput.toInt() - 1]
            } else MenuOption.valueOf(userInput)
        } catch (ex: Exception) {
            null
        }
    }

    private fun createSession() {
        val allFilmsString = filmController.getAllFilms()

        if(allFilmsString.message.isBlank()) {
            println("There is no information of saved films")
            return
        }
        println(allFilmsString)

        val filmId = getFilmId()
        if(filmId == null) {
            println("Incorrect film Id")
            return
        }



        val filmStartTime = getLocalDateTimeFromUser()
        if(filmStartTime == null) {
            println("Incorrect date or time of start")
            return
        }

        println(sessionController.addSession(filmId, filmStartTime, DI.cinemaHallSeats))
    }
    private fun editFilmDuration() {
        val allFilmsString = filmController.getAllFilms()

        if(allFilmsString.message.isBlank()) {
            println("There is no information of saved films")
            return
        }
        println(allFilmsString)

        val filmId = getFilmId()

        if(filmId == null) {
            println("Incorrect film Id")
            return
        }



        print("Input the new duration of the film in minutes: ")
        val duration = getIntegerFromUser()
        if(duration == null) {
            println("Incorrect film duration")
            return
        }

        println(filmController.editFilmDuration(filmId, duration))
    }
    private fun editFilmName() {
        println("Available films:")
        if(!showAllFilms())
            return

        val filmId = getFilmId()


        if(filmId == null) {
            println("Incorrect film Id")
            return
        }


        print("Input the new name of the film: ")
        val filmName = readlnOrNull()
        if(filmName == null) {
            println("Incorrect film name")
            return
        }

        if(filmName.isBlank()) {
            println("Film name cannot be empty")
            return
        }

        println(filmController.editFilmName(filmId, filmName))
    }

    private fun addFilmInfo() {
        print("Input the name of the film: ")
        val filmName = readlnOrNull()
        if(filmName == null) {
            println("Incorrect film name")
            return
        }

        if(filmName.isBlank()) {
            println("Film name cannot be empty")
            return
        }

        print("Input the duration of the film  in minutes: ")
        val filmDuration = getIntegerFromUser()
        if(filmDuration == null || filmDuration <= 0) {
            println("Incorrect film duration")
            return
        }

        println(filmController.addFilmInfo(filmName, filmDuration))
    }
    private fun returnTicket() {
        println("Current sessions:")
        if(!showAllSessions())
            return

        val sessionId = getSessionId()
        if (sessionId == null) {
            println("Incorrect session Id")
            return
        }

        println("Sold tickets:")
        if(!showAllTickets(sessionId))
            return


        print("Input the Id of the ticket: ")
        val ticketId = getIntegerFromUser()
        if(ticketId == null) {
            println("Incorrect ticket Id")
            return
        }

        println(ticketService.revokeTicket(sessionId, ticketId))
    }
    private fun sellTicket() {
        println("Current sessions:")
        if(!showAllSessions())
            return

        val sessionId = getSessionId()

        if (sessionId == null) {
            println("Incorrect session Id")
            return
        }

        print("Input the price of the ticket: ")

        val ticketPrice = getIntegerFromUser()
        if(ticketPrice == null || ticketPrice <= 0) {
            println("Incorrect ticket price")
            ticketPrice?.let { println("Ticket price should be a positive integer") }
            return
        }

        print("Input the number of the row: ")
        val rowNumber = getIntegerFromUser()

        print("Input the seat number: ")
        val seatNumber = getIntegerFromUser()

        if (rowNumber == null || seatNumber == null) {
            println("Incorrect row or seat format")
            return
        }

        println(ticketService.sellTicket(sessionId, ticketPrice, rowNumber, seatNumber))
    }

    private fun markOccupiedSeat() {
        println("Current sessions:")
        if(!showAllSessions())
            return

        val sessionId = getSessionId()
        if (sessionId == null) {
            println("Incorrect session Id")
            return
        }

        println("Sold tickets:")
        if(!showAllTickets(sessionId))
            return


        print("Input the number of the row: ")
        val rowNumber = getIntegerFromUser()

        print("Input the seat number: ")
        val seatNumber = getIntegerFromUser()

        if (rowNumber == null || seatNumber == null) {
            println("Incorrect row or seat format")
            return
        }

        println(sessionController.markOccupiedSeat(sessionId, rowNumber, seatNumber))
    }

    private fun editSessionStartTime() {
        println("Current sessions:")
        if(!showAllSessions())
            return

        val sessionId = getSessionId()
        if (sessionId == null) {
            println("Incorrect session Id")
            return
        }

        print("Input the new start time of the session: ")
        val newStartTime = getLocalDateTimeFromUser()

        if (newStartTime == null) {
            println("Incorrect format of time provided")
            return
        }
        println(sessionController.editSessionStartTime(sessionId, newStartTime))
    }

//    private fun editSessionFilm() {
//        println("Current sessions:")
//        if(!showAllSessions())
//            return
//
//        val sessionId = getSessionId()
//        if (sessionId == null) {
//            println("Incorrect session Id")
//            return
//        }
//
//        print("Input the Id of the new film: ")
//        val newFilmId = getIntegerFromUser()
//
//        if (newFilmId == null) {
//            println("Incorrect format of Id provided")
//            return
//        }
//        println(sessionController.editFilmId(sessionId, newFilmId))
//    }

    private fun showAvailableSeats() {
        println("Current sessions:")
        if(!showAllSessions())
            return

        val sessionId = getSessionId()

        if (sessionId == null) {
            println("Incorrect session Id")
            return
        }
        println(sessionController.getAvailableSeats(sessionId))
    }

    private fun getSessionId(): Int? {
        print("Input the Id of the session: ")
        return getIntegerFromUser()
    }

    private fun getFilmId() : Int? {
        print("Input the Id of the film: ")
        return getIntegerFromUser()
    }

    private fun getIntegerFromUser(): Int? {
        return try {
            readlnOrNull()?.toInt()
        } catch (formatException: NumberFormatException) {
            null
        }
    }

    private fun getLocalDateTimeFromUser(): LocalDateTime? {
        print("Input the local date time in the following format (dd.mm.yyyy HH:MM): ")
        val timeString = readlnOrNull() ?: return null
        return try {
            val (datePart, timePart) = timeString.split(" ")
            val (day, month, year) = datePart.split(".").map { s -> s.toInt() }
            val (hours, minutes) = timePart.split(":").map { s -> s.toInt() }

            LocalDateTime(
                year = year,
                monthNumber = month,
                dayOfMonth = day,
                hour = hours,
                minute = minutes
            )
        } catch (ex: Exception) {
            null
        }
    }


    private fun showAllSessions() : Boolean {

        val allSessionsString = sessionController.getAllSessions().message
        if(allSessionsString.isBlank()) {
            println("There are no sessions")
            return false
        }
        println(allSessionsString)
        return true
    }

    private fun showAllFilms() : Boolean{
        val allFilmsString = filmController.getAllFilms().message
        if(allFilmsString.isBlank()) {
            println("There is no information on films")
            return false
        }
        println(allFilmsString)
        return true
    }

    private fun showAllTickets(sessionId: Int) : Boolean {
        val allTicketsString = sessionController.getAllTickets(sessionId).message
        if(allTicketsString.isBlank()) {
            println("There are no sold tickets")
            return false
        }
        println(allTicketsString)
        return true
    }
}