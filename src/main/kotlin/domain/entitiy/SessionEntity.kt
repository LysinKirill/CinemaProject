package domain.entitiy

import kotlinx.datetime.LocalDateTime


data class SessionEntity(
    val sessionId: Int,
    val filmId: Int,
    val startTime: LocalDateTime,
    val soldTicketIds: Set<Int>,
    val seats: List<List<SeatState>>
)