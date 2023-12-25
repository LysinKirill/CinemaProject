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

        updatedSessions.add(session)
        val serializedUpdatedStorage = Json.encodeToString(updatedSessions.toList())
        writeTextToFile(jsonStoragePath, serializedUpdatedStorage)
    }

    override fun getSession(sessionId: Int): SessionEntity? {
        val storageFileText = readFileOrCreateEmpty(jsonStoragePath)
        val storedSessions: List<SessionEntity> =
            if (storageFileText.isBlank()) listOf() else Json.decodeFromString(storageFileText)

        return storedSessions.getOrNull(sessionId)
    }

    override fun getAllSession(): List<SessionEntity> {
        val storageFileText = readFileOrCreateEmpty(jsonStoragePath)
        val storedSessions: List<SessionEntity> =
            if (storageFileText.isBlank()) listOf() else Json.decodeFromString(storageFileText)

        return storedSessions
    }

    override fun updateWithSessions(vararg listAccount: SessionEntity) {
        val storageFileText = readFileOrCreateEmpty(jsonStoragePath)
        val storedSessions: List<SessionEntity> =
            if (storageFileText.isBlank()) listOf() else Json.decodeFromString(storageFileText)
        val updatedSessions = storedSessions.toMutableList()

        for (session in listAccount) {

            // should I subtract 1 from sessionId
            if (session.sessionId > updatedSessions.size) {
                // id check?
                updatedSessions.add(session)
            } else {
                updatedSessions[session.sessionId] = session
            }
        }
        val serializedUpdatedStorage = Json.encodeToString(updatedSessions.toList())
        writeTextToFile(jsonStoragePath, serializedUpdatedStorage)
    }

    private fun readFileOrCreateEmpty(filePath: String): String {
        val file = File(filePath)
        try {
            return file.readText()
        } catch (exception: FileNotFoundException) {
            file.createNewFile()
            //file.writeText("")
            return ""
        }
    }

    private fun writeTextToFile(filePath: String, text: String) {
        val file = File(filePath)
        file.writeText(text)
    }
}