package com.diegoparra.kino.data.network

import com.diegoparra.kino.data.network.dtos.*
import com.diegoparra.kino.models.*
import timber.log.Timber
import java.time.LocalDate

object DtoTransformations {

    //      GENRES RESPONSE TRANSFORMATION      ----------------------------------------------------

    fun GenresResponse.toGenreList() = this.genres.map { it.toGenre() }

    fun GenreDto.toGenre() = Genre(
        id = id,
        name = name ?: "Unknown"
    )


    //      MOVIE LIST RESPONSE TRANSFORMATION      ------------------------------------------------

    fun MoviesListResponse.toMoviesList() = this.results.map { it.toMovie() }

    fun MovieListItemDto.toMovie() = Movie(
        id = id,
        title = (if (originalLanguage != "en" && title != null) title else originalTitle)
            ?: "No title",
        posterUrl = MoviesApi.IMAGE_URL_PREFIX + posterPath,
        backdropUrl = MoviesApi.IMAGE_URL_PREFIX + backdropPath,
        overview = overview ?: "No description",
        genreIds = genreIds ?: emptyList(),
        rating = voteAverage?.let { (it * 10).toInt() },
        releaseDate = parseLocalDateOrNull(releaseDate, movieId = id),
        runtimeMinutes = null
    )

    //      MOVIE RESPONSE TRANSFORMATION      -----------------------------------------------------

    fun MovieDto.toMovie() = Movie(
        id = id,
        title = (if (originalLanguage != "en" && title != null) title else originalTitle)
            ?: "No title",
        posterUrl = MoviesApi.IMAGE_URL_PREFIX + posterPath,
        backdropUrl = MoviesApi.IMAGE_URL_PREFIX + backdropPath,
        overview = overview ?: "No description",
        genreIds = genres?.map { it.id } ?: emptyList(),
        rating = voteAverage?.let { (it * 10).toInt() },
        releaseDate = parseLocalDateOrNull(releaseDate, movieId = id),
        runtimeMinutes = runtime
    )


    //      CREDITS RESPONSE TRANSFORMATION      ---------------------------------------------------

    fun CreditsResponse.toMovieCredits() = MovieCredits(
        movieId = movieId,
        characters = cast
            .filter { it.knownForDepartment.equals("Acting", ignoreCase = true) }
            .map { it.toCharacter() },
        producer = crew.find { it.job.equals("Producer", ignoreCase = true) }?.toPeople()
            ?: People(
                id = "", name = "", profilePicUrl = "",
                department = People.Department.PRODUCTION, knownFor = null
            )
    )

    fun CastDto.toCharacter() = Character(
        actor = People(
            id = id,
            name = name ?: "",
            profilePicUrl = MoviesApi.IMAGE_URL_PREFIX + profilePath,
            department = People.Department.ACTING,
            knownFor = null
        ),
        character = character ?: ""
    )

    fun CrewDto.toPeople() = People(
        id = id,
        name = name ?: "",
        profilePicUrl = MoviesApi.IMAGE_URL_PREFIX + profilePath,
        department = People.Department.PRODUCTION,
        knownFor = null
    )


    //      PERSON LIST RESPONSE TRANSFORMATION      -----------------------------------------------

    fun PersonListResponse.toPeopleList() = this.results.map { it.toPeople() }

    fun PersonListItemDto.toPeople() = People(
        id = id,
        name = name ?: "",
        profilePicUrl = MoviesApi.IMAGE_URL_PREFIX + profilePath,
        department = when (knownForDepartment) {
            "Acting" -> People.Department.ACTING
            "Production" -> People.Department.PRODUCTION
            else -> People.Department.UNKNOWN
        },
        knownFor = knownFor?.map { it.toMovie() }
    )

}


/**
 * This is a safe method trying to parse a date or retrieving null if the format is not correct.
 * This is because when trying to parse a date I found some exceptions due to some values in
 * TheMovieDatabase Api.
 * - Some dates were null.
 * - Some dates were empty. Example: movieId = 829197
 *
 * In case of having a Crash Analytics service, the exception can be logged in the service, so
 * that it can be later corrected in the admin api.
 *
 * Date is intended to be formatted as: e.g. 2020-10-27
 */
private fun parseLocalDateOrNull(date: String?, movieId: String?): LocalDate? {
    return if (date.isNullOrEmpty()) {
        null
    } else {
        try {
            LocalDate.parse(date)
        } catch (e: Exception) {
            Timber.e("Couldn't parse date: $date from movieId = $movieId")
            null
        }
    }
}