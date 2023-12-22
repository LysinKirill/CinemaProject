package data

import domain.entitiy.TicketEntity

class RuntimeTicketDao : TicketDao {
    private val tickets = mutableMapOf<Int, TicketEntity>()

    override fun addTicket(ticket: TicketEntity) {
        tickets[ticket.ticketId] = ticket
    }

    override fun getTicket(ticketId: Int): TicketEntity? = tickets[ticketId]
    override fun removeTicket(ticketId: Int) {
        tickets.remove(ticketId)
    }

    override fun getAllTickets(): List<TicketEntity> = tickets.values.toList()

}