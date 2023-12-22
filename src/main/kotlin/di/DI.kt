package di

import data.RuntimeSessionDao
import data.SessionDao
import domain.SessionController
import domain.SessionControllerImpl

object DI {
    private val sessionDao: SessionDao by lazy {
        RuntimeSessionDao()
    }
    val sessionController : SessionController
        get() = SessionControllerImpl(sessionDao)
}