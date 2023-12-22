package di

import data.*
import domain.*
import presentation.ConsoleMenuManager
import presentation.MenuManager

object DI {
    private val sessionDao: SessionDao by lazy {
        RuntimeSessionDao()
    }

    private val filmDao: FilmDao by lazy {
        RuntimeFilmDao()
    }

    private val ticketDao : TicketDao by lazy {
        RuntimeTicketDao()
    }
    val sessionController : SessionController
        get() = SessionControllerImpl(sessionDao)

    val filmController : FilmController
        get() = FilmControllerImpl(filmDao)

    val ticketService : TicketService
        get() = TicketServiceImpl(sessionDao, ticketDao)

    val consoleMenuManager : MenuManager
        get() = ConsoleMenuManager(filmController, sessionController, ticketService)
}