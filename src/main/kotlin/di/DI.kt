package di

import data.*
import domain.*
import domain.entitiy.SeatState
import presentation.*

object DI {
    private const val CINEMA_ROW_COUNT = 10
    private const val ROW_SEAT_COUNT = 12
    private const val SESSION_STORAGE_PATH = "src/main/resources/session_storage.json"
    private const val FILM_STORAGE_PATH = "src/main/resources/film_storage.json"
    private const val TICKET_STORAGE_PATH = "src/main/resources/ticket_storage.json"
    private const val EMPLOYEE_STORAGE_PATH = "src/main/resources/employee_storage.json"

    private val employeeDao : EmployeeDao by lazy {
        JsonEmployeeStorage(EMPLOYEE_STORAGE_PATH)
    }

    private val sessionDao: SessionDao by lazy {
        JsonSessionStorage(SESSION_STORAGE_PATH)
    }

    private val filmDao: FilmDao by lazy {
        JsonFilmStorage(FILM_STORAGE_PATH)
    }

    private val ticketDao : TicketDao by lazy {
        JsonTicketStorage(TICKET_STORAGE_PATH)
    }

    val authorizationController : AuthorizationContoller
        get() = AuthorizationControllerImpl(employeeDao)
    val sessionController : SessionController
        get() = SessionControllerImpl(sessionDao, filmDao, ticketDao)

    val filmController : FilmController
        get() = FilmControllerImpl(filmDao)

    val ticketService : TicketService
        get() = TicketServiceImpl(sessionDao, ticketDao)

    val consoleMenuManager : MenuManager<MenuOption>
        get() = ConsoleMenuManager(filmController, sessionController, ticketService)


    val authorizationMenuManager : MenuManager<AuthorizationMenuOption>
        get() = AuthorizationMenuManager(authorizationController)

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