package domain

import data.SessionDao
import data.TicketDao
import domain.entitiy.SeatState
import domain.entitiy.TicketEntity
import presentation.model.OutputModel

class TicketServiceImpl(
    private val sessionDao: SessionDao,
    private val ticketDao: TicketDao,
) : TicketService {
    override fun sellTicket(sessionId: Int, price: Int, row: Int, seatNumber: Int): OutputModel {
        val session = sessionDao.getSession(sessionId) ?: return OutputModel("Session with Id = $sessionId not found")

        if (row < 1 || row > session.seats.size)
            return OutputModel("Incorrect row chosen [$row]. The number of rows in this session = ${session.seats.size}")

        if (seatNumber < 1 || seatNumber > session.seats[row - 1].size)
            return OutputModel("Incorrect seat number chosen [$seatNumber]. The number of seats in the row number $row in this session = ${session.seats[row - 1].size}")

        if (session.seats[row - 1][seatNumber - 1] != SeatState.Available)
            return OutputModel("The chosen seat has already been sold")


        val newTicketId = session.soldTicketIds.max() + 1
        val newTicket = TicketEntity(
            ticketId = newTicketId,
            row = row - 1,
            seatNumber = seatNumber - 1,
            price = price
        )

        ticketDao.addTicket(newTicket)
        val updatedSeatState = mutableListOf<MutableList<SeatState>>()
        for(i in session.seats.indices)
            updatedSeatState.add(session.seats[i].toMutableList())
        updatedSeatState[row][seatNumber] = SeatState.Sold

        val updatedSession = session.copy(soldTicketIds = session.soldTicketIds.plus(newTicketId), seats = updatedSeatState)

        sessionDao.updateWithSessions(updatedSession)
        return OutputModel("Ticket with Id = $newTicketId successfully sold")
    }


    override fun revokeTicket(sessionId: Int, ticketId: Int): OutputModel {
        val ticket = ticketDao.getTicket(ticketId) ?: return OutputModel("Ticket with Id = $ticketId not found")
        val session = sessionDao.getSession(sessionId) ?: return OutputModel("Session with Id = $sessionId not found")


        ticketDao.removeTicket(ticketId)
        val updatedSeatState = mutableListOf<MutableList<SeatState>>()
        for(i in session.seats.indices)
            updatedSeatState.add(session.seats[i].toMutableList())
        updatedSeatState[ticket.row][ticket.seatNumber] = SeatState.Available

        val updatedSession = session.copy(soldTicketIds = session.soldTicketIds.minus(ticketId), seats = updatedSeatState)

        sessionDao.updateWithSessions(updatedSession)
        return OutputModel("Ticket with Id = $ticketId successfully revoked")
    }
}