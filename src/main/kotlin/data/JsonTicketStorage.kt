package data

import domain.entitiy.TicketEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

class JsonTicketStorage(private val jsonStoragePath: String) : TicketDao {
    override fun addTicket(ticket: TicketEntity) {
        val storageFileText = readFileOrCreateEmpty(jsonStoragePath)
        val storedTickets: List<TicketEntity> =
            if (storageFileText.isBlank()) listOf() else Json.decodeFromString(storageFileText)
        val updatedSessions = storedTickets.toMutableList()

        updatedSessions.add(ticket)
        val serializedUpdatedStorage = Json.encodeToString(updatedSessions.toList())
        writeTextToFile(jsonStoragePath, serializedUpdatedStorage)
    }

    override fun getTicket(ticketId: Int): TicketEntity? {
        val storageFileText = readFileOrCreateEmpty(jsonStoragePath)
        val storedTickets: List<TicketEntity> =
            if (storageFileText.isBlank()) listOf() else Json.decodeFromString(storageFileText)

        return storedTickets.find { ticket -> ticket.ticketId == ticketId }
    }

    override fun removeTicket(ticketId: Int) {
        val storageFileText = readFileOrCreateEmpty(jsonStoragePath)
        val storedTickets: List<TicketEntity> =
            if (storageFileText.isBlank()) listOf() else Json.decodeFromString(storageFileText)
        val updatedSessions = storedTickets.toMutableList()
        updatedSessions.removeIf { ticket -> ticket.ticketId == ticketId }

        val serializedUpdatedStorage = Json.encodeToString(updatedSessions.toList())
        writeTextToFile(jsonStoragePath, serializedUpdatedStorage)
    }

    override fun getAllTickets(): List<TicketEntity> {
        val storageFileText = readFileOrCreateEmpty(jsonStoragePath)

        return if (storageFileText.isBlank()) listOf() else Json.decodeFromString<List<TicketEntity>>(storageFileText)
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