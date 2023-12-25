package data

import domain.entitiy.SessionEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

import java.io.FileNotFoundException

class JsonSessionStorage(private val jsonStoragePath: String) : SessionDao {
    override fun addSession(session: SessionEntity) {
        val storageFileText = readFileOrCreateEmpty(jsonStoragePath)
        val storedSessions: List<SessionEntity> =
            if (storageFileText.isBlank()) listOf() else Json.decodeFromString(storageFileText)
        val updatedSessions = storedSessions.toMutableList()

        updatedSessions.removeIf { oldSession -> oldSession.sessionId == session.sessionId }
        updatedSessions.add(session)
        val serializedUpdatedStorage = Json.encodeToString(updatedSessions.toList())
        writeTextToFile(jsonStoragePath, serializedUpdatedStorage)
    }

    override fun getSession(sessionId: Int): SessionEntity? {
        val storageFileText = readFileOrCreateEmpty(jsonStoragePath)
        val storedSessions: List<SessionEntity> =
            if (storageFileText.isBlank()) listOf() else Json.decodeFromString(storageFileText)

        return storedSessions.find { session -> session.sessionId == sessionId }
    }

    override fun getAllSessions(): List<SessionEntity> {
        val storageFileText = readFileOrCreateEmpty(jsonStoragePath)

        return if (storageFileText.isBlank()) listOf() else Json.decodeFromString<List<SessionEntity>>(storageFileText)
    }

    override fun updateWithSessions(vararg listAccount: SessionEntity) {
        val storageFileText = readFileOrCreateEmpty(jsonStoragePath)
        val storedSessions: List<SessionEntity> =
            if (storageFileText.isBlank()) listOf() else Json.decodeFromString(storageFileText)
        val updatedSessions = storedSessions.toMutableList()

        for (session in listAccount) {
            updatedSessions.removeIf { savedSession -> savedSession.sessionId == session.sessionId }
            updatedSessions.add(session)
        }
        val serializedUpdatedStorage = Json.encodeToString(updatedSessions.toList())
        writeTextToFile(jsonStoragePath, serializedUpdatedStorage)
    }

    private fun readFileOrCreateEmpty(filePath: String): String {
        val file = File(filePath)
        return try {
            file.readText()
        } catch (exception: FileNotFoundException) {
            file.createNewFile()
            ""
        }
    }

    private fun writeTextToFile(filePath: String, text: String) {
        val file = File(filePath)
        file.writeText(text)
    }
}