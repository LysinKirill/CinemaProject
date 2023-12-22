package domain

import kotlinx.datetime.LocalDateTime
import presentation.model.OutputModel


interface SessionController {
    fun editSessionStartTime(sessionId: Int, newStartTime: LocalDateTime) : OutputModel
    fun editFilmId(sessionId: Int, newFilmId: Int) : OutputModel
    fun getAvailableSeats(sessionId: Int) : OutputModel
    fun getOccupiedSeats(sessionId: Int) : OutputModel
    fun markOccupiedSeat(sessionId: Int, seatRow: Int, seatNumber: Int) : OutputModel

}
