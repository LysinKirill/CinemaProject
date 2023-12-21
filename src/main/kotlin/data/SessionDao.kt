package data

import domain.entitiy.SessionEntity

interface SessionDao {
    fun addSession(session: SessionEntity)
    fun getSession(sessionId: Int) : SessionEntity?
    fun getAllSession() : List<SessionEntity>
    fun updateWithSessions(vararg listAccount: SessionEntity)
}