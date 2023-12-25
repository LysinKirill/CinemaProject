package data

import domain.entitiy.SessionEntity

class RuntimeSessionDao : SessionDao {
    val sessions = mutableMapOf<Int, SessionEntity>()
    override fun addSession(session: SessionEntity) {
        sessions[session.sessionId] = session
    }

    override fun getSession(sessionId: Int): SessionEntity? {
        return sessions[sessionId]
    }

    override fun getAllSessions(): List<SessionEntity> {
        return sessions.values.toList()
    }

    override fun updateWithSessions(vararg listAccount: SessionEntity) {
        for(session in listAccount) {
            sessions[session.sessionId] = session
        }
    }
}