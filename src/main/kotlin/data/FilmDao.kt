package data

import domain.entitiy.FilmInfoEntity

interface FilmDao {
    fun getFilm(filmId: Int)
    fun addFilm(filmToAddId: Int, filmInfo: FilmInfoEntity)
    fun getAllFilms() : List<FilmInfoEntity>
}