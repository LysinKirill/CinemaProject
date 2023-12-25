package data

import domain.entitiy.FilmInfoEntity

interface FilmDao {
    fun getFilm(filmId: Int): FilmInfoEntity?
    fun addFilm(filmInfo: FilmInfoEntity)
    fun getAllFilms(): List<FilmInfoEntity>
    fun updateFilm(filmInfo: FilmInfoEntity)
}