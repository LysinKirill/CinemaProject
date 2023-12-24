package domain

import data.FilmDao
import domain.entitiy.FilmInfoEntity
import presentation.model.OutputModel

class FilmControllerImpl(private val filmDao: FilmDao) : FilmController {
    override fun addFilmInfo(filmName: String, duration: Int): OutputModel {
        val newFilmId = (filmDao.getAllFilms().maxOfOrNull { film -> film.filmId } ?: 0) + 1
        val newFilm = FilmInfoEntity(filmId = newFilmId, name = filmName, durationInMinutes = duration)
        filmDao.addFilm(newFilm)
        return OutputModel("Film with id $newFilmId have been added")
    }

    override fun editFilmName(filmId: Int, newName: String): OutputModel {
        val film = filmDao.getFilm(filmId) ?: return OutputModel("Film with id = $filmId not found")
        if(newName.isBlank())
            return OutputModel("Cannot assign an empty name to a film")

        filmDao.updateFilm(film.copy(name = newName))
        return OutputModel("Updated name to $newName for film with Id = $filmId")
    }

    override fun editFilmDuration(filmId: Int, newDurationInMinutes: Int): OutputModel {
        val film = filmDao.getFilm(filmId) ?: return OutputModel("Film with id = $filmId not found")
        if(newDurationInMinutes <= 0)
            return OutputModel("Duration of a film cannot be a negative number")

        filmDao.updateFilm(film.copy(durationInMinutes = newDurationInMinutes))
        return OutputModel("Updated duration to $newDurationInMinutes minutes for film with Id = $filmId")
    }
}