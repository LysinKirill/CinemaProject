package data

import domain.entitiy.TicketEntity

interface TicketDao {
    fun addTicket(ticket: TicketEntity)
    fun getTicket(ticketId: Int) : TicketEntity?
    fun removeTicket(ticketId: Int)
    fun getAllTickets() : List<TicketEntity>
}