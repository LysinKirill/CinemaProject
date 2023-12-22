package domain

import domain.entitiy.FilmInfoEntity
import presentation.model.OutputModel

interface FilmController {
    fun addFilmInfo(filmInfo: FilmInfoEntity) : OutputModel
    fun editFilmName(filmId: Int, newName: String) : OutputModel
    fun editFilmDuration(filmId: Int, newDurationInMinutes: Int) : OutputModel
}