package com.diegoparra.kino.data.network

import androidx.annotation.VisibleForTesting
import com.diegoparra.kino.data.network.MoviesDtoMappers.Companion.NO_INFO_AVAILABLE
import com.diegoparra.kino.data.network.dtos.*
import com.diegoparra.kino.models.*
import timber.log.Timber
import java.time.LocalDate

class MoviesDtoMappersImpl : MoviesDtoMappers {

    //      GENRES RESPONSE TRANSFORMATION      ----------------------------------------------------

    override fun toGenreList(genresResponse: GenresResponse): List<Genre> {
        return genresResponse.genres.map { it.toGenre() }
    }

    @VisibleForTesting
    fun GenreDto.toGenre() = Genre(
        id = id,
        name = name ?: NO_INFO_AVAILABLE
    )


    //      MOVIE LIST RESPONSE TRANSFORMATION      ------------------------------------------------

    override fun toMoviesList(moviesListResponse: MoviesListResponse): List<Movie> {
        return moviesListResponse.results.map { it.toMovie() }
    }

    @VisibleForTesting
    fun MovieListItemDto.toMovie() = Movie(
        id = id,
        title = if(!title.isNullOrBlank()) title else originalTitle ?: NO_INFO_AVAILABLE,
        posterUrl = posterPath?.let { MoviesApi.IMAGE_URL_PREFIX + it } ?: NO_INFO_AVAILABLE,
        backdropUrl = backdropPath?.let { MoviesApi.IMAGE_URL_PREFIX + it } ?: NO_INFO_AVAILABLE,
        overview = overview ?: NO_INFO_AVAILABLE,
        genreIds = genreIds ?: emptyList(),
        rating = voteAverage?.let { (it * 10).toInt() },
        releaseDate = parseLocalDateOrNull(releaseDate, movieId = id),
        runtimeMinutes = null
    )


    //      MOVIE RESPONSE TRANSFORMATION      -----------------------------------------------------

    override fun toMovie(movieResponse: MovieResponse): Movie {
        return with(movieResponse) {
            Movie(
                id = id,
                title = if(!title.isNullOrBlank()) title else originalTitle ?: NO_INFO_AVAILABLE,
                posterUrl = posterPath?.let { MoviesApi.IMAGE_URL_PREFIX + it } ?: NO_INFO_AVAILABLE,
                backdropUrl = backdropPath?.let { MoviesApi.IMAGE_URL_PREFIX + it } ?: NO_INFO_AVAILABLE,
                overview = overview ?: NO_INFO_AVAILABLE,
                genreIds = genres?.map { it.id } ?: emptyList(),
                rating = voteAverage?.let { (it * 10).toInt() },
                releaseDate = parseLocalDateOrNull(releaseDate, movieId = id),
                runtimeMinutes = runtime
            )
        }
    }


    //      CREDITS RESPONSE TRANSFORMATION      ---------------------------------------------------

    override fun toMovieCredits(creditsResponse: CreditsResponse): MovieCredits {
        return MovieCredits(
            movieId = creditsResponse.movieId,
            characters = creditsResponse.cast
                .filter { it.knownForDepartment.equals("Acting", ignoreCase = true) }
                .map { it.toCharacter() },
            producer = creditsResponse.crew
                .find { it.job.equals("Producer", ignoreCase = true) }?.toPeople()
                ?: People(
                    id = NO_INFO_AVAILABLE, name = NO_INFO_AVAILABLE, profilePicUrl = NO_INFO_AVAILABLE,
                    department = People.Department.UNKNOWN, knownFor = null
                )
        )
    }

    @VisibleForTesting
    fun CastDto.toCharacter() = Character(
        actor = People(
            id = id,
            name = name ?: NO_INFO_AVAILABLE,
            profilePicUrl = MoviesApi.IMAGE_URL_PREFIX + profilePath,
            department = People.Department.ACTING,
            knownFor = null
        ),
        character = character ?: NO_INFO_AVAILABLE
    )

    @VisibleForTesting
    fun CrewDto.toPeople() = People(
        id = id,
        name = name ?: NO_INFO_AVAILABLE,
        profilePicUrl = MoviesApi.IMAGE_URL_PREFIX + profilePath,
        department = People.Department.PRODUCTION,
        knownFor = null
    )


    //      PERSON LIST RESPONSE TRANSFORMATION      -----------------------------------------------

    override fun toPeopleList(personListResponse: PersonListResponse): List<People> {
        return personListResponse.results.map { it.toPeople() }
    }

    @VisibleForTesting
    fun PersonListItemDto.toPeople() = People(
        id = id,
        name = name ?: NO_INFO_AVAILABLE,
        profilePicUrl = MoviesApi.IMAGE_URL_PREFIX + profilePath,
        department = when (knownForDepartment) {
            "Acting" -> People.Department.ACTING
            "Production" -> People.Department.PRODUCTION
            else -> People.Department.UNKNOWN
        },
        knownFor = knownFor?.map { it.toMovie() }
    )


    //      UTILS      -----------------------------------------------------------------------------

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
    @VisibleForTesting
    fun parseLocalDateOrNull(date: String?, movieId: String? = null): LocalDate? {
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
}