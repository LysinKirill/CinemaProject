package data

import domain.entitiy.EmployeeEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

class JsonEmployeeStorage(val jsonStoragePath: String) : EmployeeDao {
    override fun getEmployee(username: String): EmployeeEntity? {
        val storageFileText = readFileOrCreateEmpty(jsonStoragePath)
        val storedEmployees: List<EmployeeEntity> =
            if (storageFileText.isBlank()) listOf() else Json.decodeFromString(storageFileText)

        return storedEmployees.find { employee -> employee.employeeUsername == username }
    }

    override fun addEmployee(employeeEntity: EmployeeEntity) {
        val storageFileText = readFileOrCreateEmpty(jsonStoragePath)
        val storedEmployees: List<EmployeeEntity> =
            if (storageFileText.isBlank()) listOf() else Json.decodeFromString(storageFileText)
        val updatedEmployees = storedEmployees.toMutableList()

        updatedEmployees.add(employeeEntity)
        val serializedUpdatedStorage = Json.encodeToString(updatedEmployees.toList())
        writeTextToFile(jsonStoragePath, serializedUpdatedStorage)
    }

    override fun getAllEmployees(): List<EmployeeEntity> {
        val storageFileText = readFileOrCreateEmpty(jsonStoragePath)
        val storedEmployees: List<EmployeeEntity> =
            if (storageFileText.isBlank()) listOf() else Json.decodeFromString(storageFileText)

        return storedEmployees
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