package di

import data.*
import domain.*
import domain.entitiy.SeatState
import presentation.ConsoleMenuManager
import presentation.MenuManager

object DI {
    private const val CINEMA_ROW_COUNT = 10
    private const val ROW_SEAT_COUNT = 12
    private const val SESSION_STORAGE_PATH = "src/main/resources/session_storage.json"

    private val sessionDao: SessionDao by lazy {
        //RuntimeSessionDao()
        JsonSessionStorage(SESSION_STORAGE_PATH)
    }

    private val filmDao: FilmDao by lazy {
        RuntimeFilmDao()
    }

    private val ticketDao : TicketDao by lazy {
        RuntimeTicketDao()
    }
    val sessionController : SessionController
        get() = SessionControllerImpl(sessionDao, filmDao)

    val filmController : FilmController
        get() = FilmControllerImpl(filmDao)

    val ticketService : TicketService
        get() = TicketServiceImpl(sessionDao, ticketDao)

    val consoleMenuManager : MenuManager
        get() = ConsoleMenuManager(filmController, sessionController, ticketService)
    //get() = ConsoleMenuManager(filmDao, sessionDao, ticketDao, filmController, sessionController, ticketService)


    val cinemaHallSeats: List<List<SeatState>>
        get() {
            val listOfRows = ArrayList<ArrayList<SeatState>>(CINEMA_ROW_COUNT)
            for(i in 0..<CINEMA_ROW_COUNT) {
                listOfRows.add(ArrayList(ROW_SEAT_COUNT))
                for(j in 0..<ROW_SEAT_COUNT) {
                    listOfRows[i].add(SeatState.Available)
                }
            }
            return listOfRows
        }
}