package domain.entitiy

data class TicketEntity(
    val ticketId: Int,
    val row: Int,
    val seatNumber: Int,
    val price: Int
)