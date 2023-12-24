package domain.entitiy

import kotlinx.serialization.Serializable

@Serializable
data class TicketEntity(
    val ticketId: Int,
    val row: Int,
    val seatNumber: Int,
    val price: Int
)