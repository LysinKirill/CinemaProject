package domain

import presentation.model.OutputModel

interface TicketService {

    fun sellTicket(sessionId: Int, price: Int, row: Int, seatNumber: Int): OutputModel
    fun revokeTicket(sessionId: Int, ticketId: Int): OutputModel
}