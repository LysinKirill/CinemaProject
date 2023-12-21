package data

import domain.entitiy.SessionEntity

interface SessionDao {
    fun add(session: SessionEntity)
    fun get(sessionId: Int) : SessionEntity?
    fun getAll() : List<SessionEntity>
    fun update(vararg listAccount: SessionEntity)
}