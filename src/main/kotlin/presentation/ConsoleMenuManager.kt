package presentation

import domain.FilmController
import domain.SessionController
import kotlinx.datetime.LocalDateTime
import java.lang.Exception

class ConsoleMenuManager(private val filmController: FilmController, private val sessionController: SessionController) : MenuManager {
    override fun showOptions() {
        print("Available actions:\n\t")
        println(MenuOption.entries.mapIndexed { index, menuAction -> "${index + 1}. $menuAction" }.joinToString("\n\t"))
    }

    override fun getRequest(): MenuOption? {
        return readlnOrNull()?.let { parseAction(it) }
    }

    override fun processRequest(request: MenuOption?) {

        //val menuOption: MenuOption? = request?.let { parseAction(it) }


        when(request) {
            MenuOption.SellTicket -> TODO()
            MenuOption.ReturnTicket -> TODO()
            MenuOption.ShowAvailableSeats -> showAvailableSeats(getSessionId())
            MenuOption.AddFilmInfo -> TODO()
            MenuOption.EditFilmInfo -> TODO()
            MenuOption.EditSessionStartTime -> editSessionStartTime(getSessionId())
            MenuOption.EditSessionFilm -> editSessionFilm(getSessionId())
            MenuOption.MarkOccupiedSeat -> markOccupiedSeat(getSessionId())
            MenuOption.Exit -> return
            else -> TODO()
        }
    }

    override fun handleInteractions() {
        do {
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

    private fun markOccupiedSeat(sessionId: Int?) {
        if(sessionId == null) {
            println("Incorrect session Id")
            return
        }

        print("Input the number of the row:")
        val rowNumber = getIntegerFromUser()

        print("Input the seat number:")
        val seatNumber = getIntegerFromUser()

        if(rowNumber == null || seatNumber == null) {
            println("Incorrect row or seat format")
            return
        }

        println(sessionController.markOccupiedSeat(sessionId, rowNumber, seatNumber))
    }
    private fun editSessionStartTime(sessionId: Int?) {
        if(sessionId == null) {
            println("Incorrect session Id")
            return
        }

        print("Input the new start time of the session: ")
        val newStartTime = getLocalTimeFromUser()

        if(newStartTime == null) {
            println("Incorrect format of time provided")
            return
        }
        println(sessionController.editSessionStartTime(sessionId, newStartTime))
    }

    private fun editSessionFilm(sessionId: Int?) {
        if(sessionId == null) {
            println("Incorrect session Id")
            return
        }

        print("Input the Id of the new film: ")
        val newFilmId = getIntegerFromUser()

        if(newFilmId == null) {
            println("Incorrect format of Id provided")
            return
        }
        println(sessionController.editFilmId(sessionId, newFilmId))
    }
    private fun showAvailableSeats(sessionId: Int?) {
        if(sessionId == null) {
            println("Incorrect session Id")
            return
        }
        println(sessionController.getAvailableSeats(sessionId))
    }
    private fun getSessionId(): Int? {
        print("Input the Id of the session: ")
        return getIntegerFromUser()
    }

    private fun getIntegerFromUser(): Int? {
        return readlnOrNull()?.toInt()
    }

    private fun getLocalTimeFromUser() : LocalDateTime? {
        print("Input the local time in the following format (HH:MM:SS): ")
        return readlnOrNull()?.let { LocalDateTime.parse(it) }
    }
}