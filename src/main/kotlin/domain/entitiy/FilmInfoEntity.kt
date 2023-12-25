package domain.entitiy

import kotlinx.serialization.Serializable

@Serializable
data class FilmInfoEntity(
    val filmId: Int,
    val name: String,
    val durationInMinutes: Int
)