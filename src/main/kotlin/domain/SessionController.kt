package domain


interface SessionController {
    fun editSessionInfo();
    fun getAvailableSeats();
    fun getOccupiedSeats();
    fun revokeTicket();
}
