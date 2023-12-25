package data

import domain.entitiy.SessionEntity

interface SessionDao {
    fun addSession(session: SessionEntity)
    fun getSession(sessionId: Int): SessionEntity?
    fun getAllSessions(): List<SessionEntity>
    fun updateWithSessions(vararg listAccount: SessionEntity)
}