package domain

import data.FilmDao
import data.SessionDao
import data.TicketDao
import domain.entitiy.SeatState
import domain.entitiy.SessionEntity
import domain.entitiy.TicketEntity
import kotlinx.datetime.LocalDateTime
import presentation.model.OutputModel

class SessionControllerImpl(
    private val sessionDao: SessionDao,
    private val filmDao: FilmDao,
    private val ticketDao: TicketDao
) : SessionController {
    override fun addSession(filmId: Int, startTime: LocalDateTime, seats: List<List<SeatState>>): OutputModel {
        if (filmDao.getAllFilms().none { film -> film.filmId == filmId })
            return OutputModel("Cannot find film with Id = $filmId")
        val newSessionId = (sessionDao.getAllSessions().maxOfOrNull { session -> session.sessionId } ?: 0) + 1
        val newSession = SessionEntity(newSessionId, filmId, startTime, setOf(), seats)
        sessionDao.addSession(newSession)
        return OutputModel("Session with id = $newSessionId was created")
    }

    override fun editSessionStartTime(sessionId: Int, newStartTime: LocalDateTime): OutputModel {
        val session = sessionDao.getSession(sessionId) ?: return OutputModel("Session with Id = $sessionId not found")
        val updatedSession = session.copy(startTime = newStartTime)
        sessionDao.updateWithSessions(updatedSession)
        return OutputModel("Session with Id = $sessionId updated with start time = $newStartTime")
    }

    override fun editFilmId(sessionId: Int, newFilmId: Int): OutputModel {
        val session = sessionDao.getSession(sessionId) ?: return OutputModel("Session with Id = $sessionId not found")
        if (filmDao.getAllFilms().none { film -> film.filmId == newFilmId })
            return OutputModel("Cannot find film with Id = $newFilmId")
        val updatedSession = session.copy(filmId = newFilmId)
        sessionDao.updateWithSessions(updatedSession)
        return OutputModel("Session with Id = $sessionId updated with filmId = $newFilmId")
    }

    override fun getAvailableSeats(sessionId: Int): OutputModel {
        val session = sessionDao.getSession(sessionId) ?: return OutputModel("Session with Id = $sessionId not found")

        val availableSeats = mutableListOf<MutableList<Int>>()
        for (i in session.seats.indices) {
            availableSeats.add(mutableListOf())
            for (j in session.seats[i].indices)
                if (session.seats[i][j] == SeatState.Available)
                    availableSeats[i].add(j)
        }

        return if (availableSeats.isEmpty() || availableSeats.sumOf { list -> list.size } == 0)
            OutputModel("All the seats are booked")
        else {
            val stringBuilder = StringBuilder()
            stringBuilder.append("Available seats for the session with Id $sessionId\n")
            for (i in availableSeats.indices) {
                if (availableSeats[i].isEmpty() || availableSeats.sumOf { list -> list.size } == 0)
                    stringBuilder.append("No seats available in row number ${i + 1}\n")
                else
                    stringBuilder.append(
                        "Available seats in row number ${i + 1}: " + availableSeats[i].joinToString(
                            separator = " ",
                            prefix = "[",
                            postfix = "]\n"
                        ) { el -> (el + 1).toString() })
            }
            return OutputModel(stringBuilder.toString())
        }

    }

    override fun getOccupiedSeats(sessionId: Int): OutputModel {
        val session = sessionDao.getSession(sessionId) ?: return OutputModel("Session with Id = $sessionId not found")

        val occupiedSeats = mutableListOf<MutableList<Int>>()
        for (i in session.seats.indices) {
            occupiedSeats.add(mutableListOf())
            for (j in session.seats[i].indices)
                if (session.seats[i][j] == SeatState.Occupied)
                    occupiedSeats[i].add(j)
        }

        return if (occupiedSeats.isEmpty() || occupiedSeats.sumOf { list -> list.size } == 0)
            OutputModel("None of the seats for the session with Id = $sessionId are occupied")
        else {
            val stringBuilder = StringBuilder()
            stringBuilder.append("Occupied seats for the session with Id $sessionId\n")
            for (i in occupiedSeats.indices) {
                if (occupiedSeats[i].isEmpty())
                    stringBuilder.append("No occupied seats in row number ${i + 1}\n")
                else
                    stringBuilder.append(
                        "Occupied seats in row number ${i + 1}: " + occupiedSeats[i].joinToString(
                            separator = " ",
                            prefix = "[",
                            postfix = "]\n"
                        ) { el -> (el + 1).toString() })
            }
            return OutputModel(stringBuilder.toString())
        }
    }

    override fun markOccupiedSeat(sessionId: Int, seatRow: Int, seatNumber: Int): OutputModel {
        val session = sessionDao.getSession(sessionId) ?: return OutputModel("Session with Id = $sessionId not found")

        if (seatRow < 1 || seatRow > session.seats.size)
            return OutputModel("Incorrect row chosen [$seatRow]. The number of rows in this session = ${session.seats.size}")

        if (seatNumber < 1 || seatNumber > session.seats[seatRow - 1].size)
            return OutputModel("Incorrect seat number chosen [$seatNumber]. The number of seats in the row number $seatRow in this session = ${session.seats[seatRow - 1].size}")

        if (session.seats[seatRow - 1][seatNumber - 1] == SeatState.Occupied)
            return OutputModel("The seat is already marked as occupied")

        if (session.seats[seatRow - 1][seatNumber - 1] == SeatState.Available)
            return OutputModel("Cannot mark the seat which have not been sold as occupied")

        val updatedSeatState = mutableListOf<MutableList<SeatState>>()
        for (i in session.seats.indices)
            updatedSeatState.add(session.seats[i].toMutableList())

        updatedSeatState[seatRow][seatNumber] = SeatState.Occupied

        val updatedSession = session.copy(seats = updatedSeatState)
        sessionDao.updateWithSessions(updatedSession)

        return OutputModel("The seat has been successfully marked as occupied")
    }

    override fun getAllTickets(sessionId: Int): OutputModel {
        val session = sessionDao.getSession(sessionId) ?: return OutputModel("Session with Id = $sessionId not found")
        val ticketsList = mutableListOf<TicketEntity>()
        for (ticketId in session.soldTicketIds) {
            val ticket = ticketDao.getTicket(ticketId) ?: continue
            ticketsList.add(ticket)
        }

        if (ticketsList.isEmpty())
            return OutputModel("")
        return OutputModel("List of tickets for the session with Id = $sessionId:\n" +
                ticketsList.joinToString("\n") { ticket ->
                    "\tTicket with id ${ticket.ticketId}: (Row = ${ticket.row}," +
                            " Seat number = ${ticket.seatNumber}, Price = ${ticket.price})"
                })
    }

    override fun getAllSessions(): OutputModel {

        val sessions = sessionDao.getAllSessions()
        if (sessions.isEmpty())
            return OutputModel("")
        val stringBuilder = StringBuilder()
        stringBuilder.append("List of all the sessions:\n")
        for (session in sessions) {
            try {
                val filmInfo = filmDao.getFilm(session.sessionId) ?: continue
                stringBuilder.append("\tSession with id ${session.sessionId}: (film title = ${filmInfo.name}, start time = ${session.startTime})\n")
            } catch (ex: Exception) {
                continue
            }
        }

        return OutputModel(stringBuilder.toString())
    }
}