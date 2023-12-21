package domain

import kotlinx.datetime.LocalDateTime
import presentation.model.OutputModel


interface SessionController {
    //fun editSessionInfo(sessionId: Int, )

    fun editSessionStartTime(sessionId: Int, newStartTime: LocalDateTime) : OutputModel
    fun editFilmId(sessionId: Int, newFilmId: Int) : OutputModel
    fun getAvailableSeats(sessionId: Int) : OutputModel
    fun getOccupiedSeats(sessionId: Int) : OutputModel
    fun revokeTicket(sessionId: Int, ticketId: Int) : OutputModel

}
