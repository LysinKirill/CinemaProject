package data

import domain.entitiy.FilmInfoEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

class JsonFilmStorage(private val jsonStoragePath: String) : FilmDao {
    override fun getFilm(filmId: Int): FilmInfoEntity? {
        val storageFileText = readFileOrCreateEmpty(jsonStoragePath)
        val storedFilms: List<FilmInfoEntity> =
            if (storageFileText.isBlank()) listOf() else Json.decodeFromString(storageFileText)

        return storedFilms.find { film -> film.filmId == filmId }
    }

    override fun addFilm(filmInfo: FilmInfoEntity) {
        val storageFileText = readFileOrCreateEmpty(jsonStoragePath)
        val storedFilms: List<FilmInfoEntity> =
            if (storageFileText.isBlank()) listOf() else Json.decodeFromString(storageFileText)
        val updatedFilms = storedFilms.toMutableList()

        updatedFilms.add(filmInfo)
        val serializedUpdatedStorage = Json.encodeToString(updatedFilms.toList())
        writeTextToFile(jsonStoragePath, serializedUpdatedStorage)
    }

    override fun getAllFilms(): List<FilmInfoEntity> {
        val storageFileText = readFileOrCreateEmpty(jsonStoragePath)
        val storedFilms: List<FilmInfoEntity> =
            if (storageFileText.isBlank()) listOf() else Json.decodeFromString(storageFileText)

        return storedFilms
    }

    override fun updateFilm(filmInfo: FilmInfoEntity) {
        val storageFileText = readFileOrCreateEmpty(jsonStoragePath)
        val storedFilms: List<FilmInfoEntity> =
            if (storageFileText.isBlank()) listOf() else Json.decodeFromString(storageFileText)
        val updatedFilms = storedFilms.toMutableList()

        updatedFilms.removeIf { savedFilm -> savedFilm.filmId == filmInfo.filmId }
        updatedFilms.add(filmInfo)

        val serializedUpdatedStorage = Json.encodeToString(updatedFilms.toList())
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