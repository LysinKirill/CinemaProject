package domain

import data.FilmDao
import domain.entitiy.FilmInfoEntity
import presentation.model.OutputModel

class FilmControllerImpl(private val filmDao: FilmDao) : FilmController {
    override fun addFilmInfo(filmInfo: FilmInfoEntity): OutputModel {
        if(filmDao.getFilm(filmInfo.filmId) != null)
            return OutputModel("Film with id ${filmInfo.filmId} already exists")

        filmDao.addFilm(filmInfo)
        return OutputModel("Film with id ${filmInfo.filmId} have been added")
    }

    override fun editFilmName(filmId: Int, newName: String): OutputModel {
        val film = filmDao.getFilm(filmId) ?: return OutputModel("Film with id = $filmId not found")

        filmDao.updateFilm(film.copy(name = newName))
        return OutputModel("Updated name to $newName for film with Id = $filmId")
    }

    override fun editFilmDuration(filmId: Int, newDurationInMinutes: Int): OutputModel {
        val film = filmDao.getFilm(filmId) ?: return OutputModel("Film with id = $filmId not found")

        filmDao.updateFilm(film.copy(durationInMinutes = newDurationInMinutes))
        return OutputModel("Updated duration to $newDurationInMinutes minutes for film with Id = $filmId")
    }
}