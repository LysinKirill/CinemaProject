package data

import domain.entitiy.FilmInfoEntity

class RuntimeFilmDao : FilmDao {
    val films = mutableMapOf<Int, FilmInfoEntity>()
    override fun getFilm(filmId: Int): FilmInfoEntity? = films[filmId]

    override fun addFilm(filmInfo: FilmInfoEntity) {
        if (!films.contains(filmInfo.filmId))
            films[filmInfo.filmId] = filmInfo
    }

    override fun getAllFilms(): List<FilmInfoEntity> = films.values.toList()

    override fun updateFilm(filmInfo: FilmInfoEntity) {
        films[filmInfo.filmId] = filmInfo
    }
}