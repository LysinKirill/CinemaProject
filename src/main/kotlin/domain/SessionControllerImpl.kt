package domain

import data.SessionDao
import domain.entitiy.SeatState
import kotlinx.datetime.LocalDateTime
import presentation.model.OutputModel

class SessionControllerImpl(private val sessionDao: SessionDao) : SessionController {
    override fun editSessionStartTime(sessionId: Int, newStartTime: LocalDateTime): OutputModel {
        TODO("Not yet implemented")
    }

    override fun editFilmId(sessionId: Int, newFilmId: Int): OutputModel {
        val session = sessionDao.getSession(sessionId) ?: return OutputModel("Session with Id = $sessionId not found")
        sessionDao.updateWithSessions(
            session.copy(
                sessionId = session.sessionId,
                filmId = newFilmId,
                startTime = session.startTime,
                soldTicketIds = session.soldTicketIds,
                seats = session.seats
            )
        )
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

        return if (availableSeats.isEmpty())
            OutputModel("All the seats are booked")
        else OutputModel(
            "Available seats for the session with Id = $sessionId\n"
                    + availableSeats.joinToString { (rowNumber, row) -> "Row number $rowNumber: $row" })

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

        return if (occupiedSeats.isEmpty())
            OutputModel("None of the seats for the session with Id = $sessionId are occupied")
        else OutputModel(
            "Occupied seats for the session with Id $sessionId\n"
                    + occupiedSeats.joinToString { (rowNumber, row) -> "Row number $rowNumber: $row" })
    }

    override fun markOccupiedSeat(sessionId: Int, seatRow: Int, seatNumber: Int): OutputModel {
        val session = sessionDao.getSession(sessionId) ?: return OutputModel("Session with Id = $sessionId not found")

        if (seatRow < 1 || seatRow > session.seats.size)
            return OutputModel("Incorrect row chosen [$seatRow]. The number of rows in this session = ${session.seats.size}")

        if (seatNumber < 1 || seatNumber > session.seats[seatRow - 1].size)
            return OutputModel("Incorrect seat number chosen [$seatNumber]. The number of seats in the row number $seatRow in this session = ${session.seats[seatRow - 1].size}")

        if(session.seats[seatRow][seatNumber] == SeatState.Occupied)
            return OutputModel("The seat is already marked as occupied")


        val updatedSeatState = mutableListOf<MutableList<SeatState>>()
        for(i in session.seats.indices)
            updatedSeatState.add(session.seats[i].toMutableList())

        updatedSeatState[seatRow][seatNumber] = SeatState.Occupied

        val updatedSession = session.copy(seats = updatedSeatState)
        sessionDao.updateWithSessions(updatedSession)

        return OutputModel("The seat has been successfully marked as occupied")
    }
}