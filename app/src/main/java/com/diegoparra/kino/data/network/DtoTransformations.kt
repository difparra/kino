package com.diegoparra.kino.data.network

import com.diegoparra.kino.data.network.dtos.*
import com.diegoparra.kino.models.Genre
import com.diegoparra.kino.models.Movie
import java.time.LocalDate

private const val IMAGE_URL_PREFIX = "https://image.tmdb.org/t/p/original"

object DtoTransformations {

    fun GenresResponse.toGenreList() = this.genres.map { it.toGenre() }

    fun GenreDto.toGenre() = Genre(
        id = id,
        name = name ?: "Unknown"
    )

    fun MoviesByGenreResponse.toMoviesList() = this.results.map { it.toMovie() }

    fun MovieListDto.toMovie() = Movie(
        id = id,
        title = (if (originalLanguage != "en" && title != null) title else originalTitle)
            ?: "No title",
        posterUrl = IMAGE_URL_PREFIX + posterPath,
        backdropUrl = IMAGE_URL_PREFIX + backdropPath,
        overview = overview ?: "No description",
        genreIds = genreIds ?: emptyList(),
        rating = voteAverage,
        releaseDate = releaseDate?.let { LocalDate.parse(it) },
        runtimeMinutes = null
    )

    fun MovieDto.toMovie() = Movie(
        id = id,
        title = (if (originalLanguage != "en" && title != null) title else originalTitle)
            ?: "No title",
        posterUrl = IMAGE_URL_PREFIX + posterPath,
        backdropUrl = IMAGE_URL_PREFIX + backdropPath,
        overview = overview ?: "No description",
        genreIds = genres?.map { it.id } ?: emptyList(),
        rating = voteAverage,
        releaseDate = releaseDate?.let { LocalDate.parse(it) },
        runtimeMinutes = runtime
    )

}