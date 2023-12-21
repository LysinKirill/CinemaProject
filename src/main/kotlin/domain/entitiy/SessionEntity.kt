package domain.entitiy

import kotlinx.datetime.LocalDateTime


data class SessionEntity(
    val filmId: Int,
    val startTime: LocalDateTime,
    val soldTicketIds: List<Int> = listOf(),
    val occupiedSeats: List<List<SeatStateEntity>>
) {
}