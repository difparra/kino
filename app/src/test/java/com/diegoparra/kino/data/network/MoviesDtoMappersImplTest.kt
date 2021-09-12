package com.diegoparra.kino.data.network

import com.diegoparra.kino.data.network.dtos.*
import com.diegoparra.kino.models.Character
import com.diegoparra.kino.models.Genre
import com.diegoparra.kino.models.Movie
import com.diegoparra.kino.models.People
import com.diegoparra.kino.test_utils.FakeDtoData
import com.diegoparra.kino.test_utils.Patterns
import com.diegoparra.kino.test_utils.assertThrows
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import org.junit.Test
import java.time.LocalDate

class MoviesDtoMappersImplTest {

    private val mapper = MoviesDtoMappersImpl()
    private val NO_INFO_AVAILABLE = MoviesDtoMappers.NO_INFO_AVAILABLE
    private val IMAGE_URL_PREFIX = MoviesApi.IMAGE_URL_PREFIX

    //      GENRES RESPONSE TRANSFORMATION      ----------------------------------------------------

    @Test
    fun toGenre_rightGenreDto_returnCorrectGenre() {
        val genreDto = GenreDto("1", "Action")
        val result = with(mapper) { genreDto.toGenre() }
        assertThat(result).isEqualTo(Genre("1", "Action"))
    }

    @Test
    fun toGenre_idIsNull_throwException() {
        val genreDto = Gson().fromJson("{}", GenreDto::class.java)
        assertThat(genreDto).isNotNull()
        assertThat(genreDto.id).isNull()

        assertThrows<Exception> { with(mapper) { genreDto.toGenre() } }
    }

    @Test
    fun toGenre_nameIsNull_returnGenreWithDefaults() {
        val genreDto = GenreDto("1", null)
        assertThat(genreDto).isNotNull()
        assertThat(genreDto.name).isNull()

        val result = with(mapper) { genreDto.toGenre() }

        assertThat(result).isEqualTo(Genre(genreDto.id, NO_INFO_AVAILABLE))
    }

    @Test
    fun toGenreList_genreResponseEmptyList_returnEmptyList() {
        val genreResponse = GenresResponse(emptyList())
        val result = mapper.toGenreList(genreResponse)
        assertThat(result).isNotNull()
        assertThat(result).isEmpty()
    }

    @Test
    fun toGenreList_successfulGenreList_returnMappedGenreList() {
        val genre1Dto = FakeDtoData.Genres.action
        val parsedGenre1 = with(mapper) { genre1Dto.toGenre() }
        val genre2Dto = FakeDtoData.Genres.comedy
        val parsedGenre2 = with(mapper) { genre2Dto.toGenre() }
        val genresResponse = GenresResponse(listOf(genre1Dto, genre2Dto))

        val result = mapper.toGenreList(genresResponse)

        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(parsedGenre1, parsedGenre2)
    }


    //      MOVIE LIST RESPONSE TRANSFORMATION      ------------------------------------------------

    @Test
    fun movieListItemDtoToMovie_rightMovieDto_returnCorrectMovie() {
        val movie = MovieListItemDto(
            id = "436969",
            adult = false,
            genreIds = listOf("1", "2", "3"),
            backdropPath = "/jlGmlFOcfo8n5tURmhC7YVd4Iyy.jpg",
            posterPath = "/fPJWlhXA2VXf4MlQ3JenVsz1iba.jpg",
            originalTitle = "The Suicide Squad",
            title = "El Escuadrón Suicida",
            originalLanguage = "en",
            overview = "Un grupo de super villanos se encuentran encerrados en Belle Reve, una prisión de alta seguridad con la tasa de mortalidad más alta de Estados Unidos. Para salir de allí harán cualquier cosa, incluso unirse al grupo Task Force X, dedicado a llevar a cabo misiones suicidas bajo las órdenes de Amanda Waller. Fuertemente armados son enviados a la isla Corto Maltese, una jungla repleta de enemigos.",
            releaseDate = "2021-07-28",
            voteAverage = 8f
        )
        val result = with(mapper) { movie.toMovie() }
        assertThat(result).isEqualTo(
            Movie(
                id = "436969",
                title = "El Escuadrón Suicida",
                posterUrl = IMAGE_URL_PREFIX + "/fPJWlhXA2VXf4MlQ3JenVsz1iba.jpg",
                backdropUrl = IMAGE_URL_PREFIX + "/jlGmlFOcfo8n5tURmhC7YVd4Iyy.jpg",
                overview = "Un grupo de super villanos se encuentran encerrados en Belle Reve, una prisión de alta seguridad con la tasa de mortalidad más alta de Estados Unidos. Para salir de allí harán cualquier cosa, incluso unirse al grupo Task Force X, dedicado a llevar a cabo misiones suicidas bajo las órdenes de Amanda Waller. Fuertemente armados son enviados a la isla Corto Maltese, una jungla repleta de enemigos.",
                genreIds = listOf("1", "2", "3"),
                rating = 80,
                releaseDate = LocalDate.of(2021, 7, 28),
                runtimeMinutes = null
            )
        )
    }

    @Test
    fun movieListItemDtoToMovie_idIsNull_throwException() {
        val movieListItemDto = Gson().fromJson("{}", MovieListItemDto::class.java)
        assertThat(movieListItemDto).isNotNull()
        assertThat(movieListItemDto.id).isNull()

        assertThrows<Exception> { with(mapper) { movieListItemDto.toMovie() } }
    }

    @Test
    fun movieListItemDtoToMovie_nullValues_returnCorrectDefaults() {
        val movie =
            MovieListItemDto("1", null, null, null, null, null, null, null, null, null, null)
        val result = with(mapper) { movie.toMovie() }
        assertThat(result).isNotNull()
        assertThat(result.id).isEqualTo(movie.id)
        assertThat(result.title).isEqualTo(NO_INFO_AVAILABLE)
        assertThat(result.posterUrl).isEqualTo(NO_INFO_AVAILABLE)
        assertThat(result.backdropUrl).isEqualTo(NO_INFO_AVAILABLE)
        assertThat(result.overview).isEqualTo(NO_INFO_AVAILABLE)
        assertThat(result.genreIds).isEmpty()
        assertThat(result.rating).isNull()
        assertThat(result.releaseDate).isNull()
        assertThat(result.runtimeMinutes).isNull()
    }

    @Test
    fun movieListItemDtoToMovie_partialImageUrlFromApi_returnValidCompleteImageUrlInResult() {
        val movie = FakeDtoData.Movies.suicideSquad.movieListItemDto.copy(
            backdropPath = "/jlGmlFOcfo8n5tURmhC7YVd4Iyy.jpg",
            posterPath = "/fPJWlhXA2VXf4MlQ3JenVsz1iba.jpg"
        )
        val result = with(mapper) { movie.toMovie() }
        assertThat(isValidUrl(result.backdropUrl)).isTrue()
        assertThat(result.backdropUrl).isEqualTo(IMAGE_URL_PREFIX + movie.backdropPath)
        assertThat(isValidUrl(result.posterUrl)).isTrue()
        assertThat(result.posterUrl).isEqualTo(IMAGE_URL_PREFIX + movie.posterPath)
    }

    @Test
    fun movieListItemDtoToMovie_releaseDateIsEmpty_returnNullForDate() {
        val movie = FakeDtoData.Movies.suicideSquad.movieListItemDto.copy(
            releaseDate = ""
        )
        val result = with(mapper) { movie.toMovie() }
        assertThat(result.releaseDate).isNull()
    }

    @Test
    fun movieListItemDtoToMovie_releaseDateIsNotValid_returnNullForDate() {
        val movie = FakeDtoData.Movies.suicideSquad.movieListItemDto.copy(
            releaseDate = "12-"
        )
        val result = with(mapper) { movie.toMovie() }
        assertThat(result.releaseDate).isNull()
    }

    @Test
    fun movieListItemDtoToMovie_correctDate_returnCorrectDate() {
        val movie = FakeDtoData.Movies.suicideSquad.movieListItemDto.copy(
            releaseDate = "2021-07-28"
        )
        val result = with(mapper) { movie.toMovie() }
        assertThat(result.releaseDate).isEqualTo(LocalDate.of(2021, 7, 28))
    }

    @Test
    fun movieListItemDtoToMovie_nullTitleButFilledOriginalTitle_returnOriginalTitle() {
        val movie = FakeDtoData.Movies.suicideSquad.movieListItemDto.copy(
            title = null,
            originalTitle = "title"
        )
        val result = with(mapper) { movie.toMovie() }
        assertThat(result.title).isEqualTo(movie.originalTitle)

    }

    @Test
    fun movieListItemDtoToMovie_blankOrEmptyTitleButFilledOriginalTitle_returnOriginalTitle() {
        val movie = FakeDtoData.Movies.suicideSquad.movieListItemDto.copy(
            title = " ",
            originalTitle = "title"
        )
        assertThat(movie.title!!.isBlank()).isTrue()
        val result = with(mapper) { movie.toMovie() }
        assertThat(result.title).isEqualTo(movie.originalTitle)
    }

    @Test
    fun toMovieList_successfulMoviesList_returnMappedMoviesList() {
        val movie = FakeDtoData.Movies.suicideSquad.movieListItemDto
        val parsedMovie = with(mapper) { movie.toMovie() }
        val moviesListResponse = MoviesListResponse(1, listOf(movie), 1, 1)
        val result = mapper.toMoviesList(moviesListResponse)
        assertThat(result).isEqualTo(listOf(parsedMovie))
    }


    //      MOVIE RESPONSE TRANSFORMATION      -----------------------------------------------------

    @Test
    fun toMovie_rightMovieDto_returnCorrectMovie() {
        val movie = MovieResponse(
            id = "436969",
            adult = false,
            genres = listOf(GenreDto("1", "Action"), GenreDto("2", "Comedy")),
            backdropPath = "/jlGmlFOcfo8n5tURmhC7YVd4Iyy.jpg",
            posterPath = "/fPJWlhXA2VXf4MlQ3JenVsz1iba.jpg",
            originalTitle = "The Suicide Squad",
            title = "El Escuadrón Suicida",
            originalLanguage = "en",
            overview = "Un grupo de super villanos se encuentran encerrados en Belle Reve, una prisión de alta seguridad con la tasa de mortalidad más alta de Estados Unidos. Para salir de allí harán cualquier cosa, incluso unirse al grupo Task Force X, dedicado a llevar a cabo misiones suicidas bajo las órdenes de Amanda Waller. Fuertemente armados son enviados a la isla Corto Maltese, una jungla repleta de enemigos.",
            releaseDate = "2021-07-28",
            voteAverage = 8f,
            homepageUrl = null,
            imdbId = null,
            runtime = 120,
            status = null,
            tagline = null
        )
        val result = mapper.toMovie(movie)
        assertThat(result).isEqualTo(
            Movie(
                id = "436969",
                title = "El Escuadrón Suicida",
                posterUrl = IMAGE_URL_PREFIX + "/fPJWlhXA2VXf4MlQ3JenVsz1iba.jpg",
                backdropUrl = IMAGE_URL_PREFIX + "/jlGmlFOcfo8n5tURmhC7YVd4Iyy.jpg",
                overview = "Un grupo de super villanos se encuentran encerrados en Belle Reve, una prisión de alta seguridad con la tasa de mortalidad más alta de Estados Unidos. Para salir de allí harán cualquier cosa, incluso unirse al grupo Task Force X, dedicado a llevar a cabo misiones suicidas bajo las órdenes de Amanda Waller. Fuertemente armados son enviados a la isla Corto Maltese, una jungla repleta de enemigos.",
                genreIds = listOf("1", "2"),
                rating = 80,
                releaseDate = LocalDate.of(2021, 7, 28),
                runtimeMinutes = 120
            )
        )
    }

    @Test
    fun toMovie_idIsNull_throwException() {
        val movieResponse = Gson().fromJson("{}", MovieResponse::class.java)
        assertThat(movieResponse).isNotNull()
        assertThat(movieResponse.id).isNull()

        assertThrows<Exception> { mapper.toMovie(movieResponse) }
    }

    @Test
    fun toMovie_nullValues_returnCorrectDefaults() {
        val movie = MovieResponse(
            "1",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        val result = mapper.toMovie(movie)
        assertThat(result).isNotNull()
        assertThat(result.id).isEqualTo(movie.id)
        assertThat(result.title).isEqualTo(NO_INFO_AVAILABLE)
        assertThat(result.posterUrl).isEqualTo(NO_INFO_AVAILABLE)
        assertThat(result.backdropUrl).isEqualTo(NO_INFO_AVAILABLE)
        assertThat(result.overview).isEqualTo(NO_INFO_AVAILABLE)
        assertThat(result.genreIds).isEmpty()
        assertThat(result.rating).isNull()
        assertThat(result.releaseDate).isNull()
        assertThat(result.runtimeMinutes).isNull()
    }

    @Test
    fun toMovie_partialImageUrlFromApi_returnValidCompleteImageUrlInResult() {
        val movie = FakeDtoData.Movies.suicideSquad.movieResponse.copy(
            backdropPath = "/jlGmlFOcfo8n5tURmhC7YVd4Iyy.jpg",
            posterPath = "/fPJWlhXA2VXf4MlQ3JenVsz1iba.jpg"
        )
        val result = mapper.toMovie(movie)
        assertThat(isValidUrl(result.backdropUrl)).isTrue()
        assertThat(result.backdropUrl).isEqualTo(IMAGE_URL_PREFIX + movie.backdropPath)
        assertThat(isValidUrl(result.posterUrl)).isTrue()
        assertThat(result.posterUrl).isEqualTo(IMAGE_URL_PREFIX + movie.posterPath)
    }

    @Test
    fun toMovie_releaseDateIsEmpty_returnNullForDate() {
        val movie = FakeDtoData.Movies.suicideSquad.movieResponse.copy(
            releaseDate = ""
        )
        val result = mapper.toMovie(movie)
        assertThat(result.releaseDate).isNull()
    }

    @Test
    fun toMovie_releaseDateIsNotValid_returnNullForDate() {
        val movie = FakeDtoData.Movies.suicideSquad.movieResponse.copy(
            releaseDate = "12-"
        )
        val result = mapper.toMovie(movie)
        assertThat(result.releaseDate).isNull()
    }

    @Test
    fun toMovie_correctDate_returnCorrectDate() {
        val movie = FakeDtoData.Movies.suicideSquad.movieResponse.copy(
            releaseDate = "2021-07-28"
        )
        val result = mapper.toMovie(movie)
        assertThat(result.releaseDate).isEqualTo(LocalDate.of(2021, 7, 28))
    }

    @Test
    fun toMovie_nullTitleButFilledOriginalTitle_returnOriginalTitle() {
        val movie = FakeDtoData.Movies.suicideSquad.movieResponse.copy(
            title = null,
            originalTitle = "title"
        )
        val result = mapper.toMovie(movie)
        assertThat(result.title).isEqualTo(movie.originalTitle)

    }

    @Test
    fun toMovie_blankOrEmptyTitleButFilledOriginalTitle_returnOriginalTitle() {
        val movie = FakeDtoData.Movies.suicideSquad.movieResponse.copy(
            title = " ",
            originalTitle = "title"
        )
        assertThat(movie.title!!.isBlank()).isTrue()
        val result = mapper.toMovie(movie)
        assertThat(result.title).isEqualTo(movie.originalTitle)
    }


    //      CREDITS RESPONSE TRANSFORMATION      ---------------------------------------------------

    @Test
    fun toMovieCredits_rightCredits_returnCorrectMovieCredits() {
        val creditsResponse = FakeDtoData.Movies.suicideSquad.credits
        val result = mapper.toMovieCredits(creditsResponse)
        assertThat(result).isNotNull()
        assertThat(result.movieId).isEqualTo(creditsResponse.movieId)
        assertThat(result.characters[0]).isEqualTo(with(mapper) {
            creditsResponse.cast.first {
                it.knownForDepartment.equals("Acting", ignoreCase = true)
            }.toCharacter()
        })
        assertThat(result.producer).isEqualTo(with(mapper) {
            creditsResponse.crew.first {
                it.job.equals("Producer", ignoreCase = true)
            }.toPeople()
        })
    }

    @Test
    fun toCharacter_rightCastDto_returnCorrectCharacter() {
        val castDto = CastDto(
            id = "234352",
            knownForDepartment = "Acting",
            name = "Margot Robbie",
            profilePath = "/euDPyqLnuwaWMHajcU3oZ9uZezR.jpg",
            character = "Harleen Quinzel / Harley Quinn"
        )
        val result = with(mapper) { castDto.toCharacter() }

        assertThat(result).isEqualTo(
            Character(
                actor = People(
                    id = "234352",
                    name = "Margot Robbie",
                    profilePicUrl = IMAGE_URL_PREFIX + "/euDPyqLnuwaWMHajcU3oZ9uZezR.jpg",
                    department = People.Department.ACTING,
                    knownFor = null
                ),
                character = "Harleen Quinzel / Harley Quinn"
            )
        )
    }

    @Test
    fun toPeople_correctCrew_returnCorrectPeople() {
        val crewDto = CrewDto(
            id = "282",
            knownForDepartment = "Production",
            name = "Charles Roven",
            profilePath = "/4uJLoVstC1CBcArXFOe53N2fDr1.jpg",
            job = "Producer"
        )
        val result = with(mapper) { crewDto.toPeople() }
        assertThat(result).isEqualTo(
            People(
                id = "282",
                name = "Charles Roven",
                profilePicUrl = IMAGE_URL_PREFIX + "/4uJLoVstC1CBcArXFOe53N2fDr1.jpg",
                department = People.Department.PRODUCTION,
                knownFor = null
            )
        )
    }


    //      PERSON LIST RESPONSE TRANSFORMATION      -----------------------------------------------

    @Test
    fun toPeopleList_rightPersonListResponse_returnCorrectPeopleList() {
        val personListResponse = PersonListResponse(
            1,
            listOf(FakeDtoData.People.margotRobbie, FakeDtoData.People.tomCruise),
            20,
            400
        )
        val result = mapper.toPeopleList(personListResponse)

        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(
            with(mapper) { FakeDtoData.People.margotRobbie.toPeople() },
            with(mapper) { FakeDtoData.People.tomCruise.toPeople() }
        )
    }

    @Test
    fun toPeople_rightPersonListItemDto_returnCorrectPeople() {
        val personListItemDto = PersonListItemDto(
            id = "234352",
            name = "Margot Robbie",
            profilePath = "/euDPyqLnuwaWMHajcU3oZ9uZezR.jpg",
            knownForDepartment = "Acting",
            knownFor = listOf(FakeDtoData.Movies.suicideSquad.movieListItemDto)
        )
        val result = with(mapper) { personListItemDto.toPeople() }
        assertThat(result).isEqualTo(
            People(
                id = "234352",
                name = "Margot Robbie",
                profilePicUrl = IMAGE_URL_PREFIX + "/euDPyqLnuwaWMHajcU3oZ9uZezR.jpg",
                department = People.Department.ACTING,
                knownFor = listOf(with(mapper) { FakeDtoData.Movies.suicideSquad.movieListItemDto.toMovie() })
            )
        )
    }


    //      UTILS      -----------------------------------------------------------------------------

    @Test
    fun parseLocalDateOrNull_correctDate_returnLocalDate() {
        assertThat(mapper.parseLocalDateOrNull("2021-09-07")).isEqualTo(LocalDate.of(2021, 9, 7))
    }

    @Test
    fun parseLocalDateOrNull_nullString_returnLocalDate() {
        assertThat(mapper.parseLocalDateOrNull(null)).isNull()
    }

    @Test
    fun parseLocalDateOrNull_emptyString_returnNull() {
        assertThat(mapper.parseLocalDateOrNull("")).isNull()
    }

    @Test
    fun parseLocalDateOrNull_incorrectFormat_returnNull() {
        assertThat(mapper.parseLocalDateOrNull("123")).isNull()
        assertThat(mapper.parseLocalDateOrNull("2021.09.18")).isNull()
    }


    @Test
    fun isValidUrlTest() {
        assertThat(isValidUrl("https://www.google.com")).isTrue()
        assertThat(isValidUrl("adskf")).isFalse()
    }

    private fun isValidUrl(url: String): Boolean {
        //  Use custom Patterns object class, as Patterns from android utils is null/hidden
        //  in android 12 (api 31) as source set in sdk manager cannot be downloaded.
        return Patterns.WEB_URL.matcher(url).matches()
    }


}