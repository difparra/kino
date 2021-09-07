package com.diegoparra.kino.test_utils

import com.diegoparra.kino.data.network.dtos.*
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class LoadJSONFromAssetsTest {

    @Test
    fun load_genres() {
        val genres: GenresResponse =
            loadJSONFromResourcesFile(Filenames.GENRES_FILENAME, GenresResponse::class.java)
        assertThat(genres).isNotNull()
        assertThat(genres.genres).isNotEmpty()
        assertThat(genres.genres).contains(GenreDto(id = "28", name = "Acción"))
    }

    @Test
    fun load_movieCredits() {
        val credits =
            loadJSONFromResourcesFile(Filenames.MOVIE_CREDITS_FILENAME, CreditsResponse::class.java)
        assertThat(credits).isNotNull()
        assertThat(credits.movieId).isEqualTo("436969")
        assertThat(credits.cast).contains(
            CastDto(
                id = "234352",
                knownForDepartment = "Acting",
                name = "Margot Robbie",
                profilePath = "/euDPyqLnuwaWMHajcU3oZ9uZezR.jpg",
                character = "Harleen Quinzel / Harley Quinn"
            )
        )
        assertThat(credits.crew).contains(
            CrewDto(
                id = "282",
                knownForDepartment = "Production",
                name = "Charles Roven",
                profilePath = "/4uJLoVstC1CBcArXFOe53N2fDr1.jpg",
                job = "Producer"
            )
        )
    }

    @Test
    fun load_movieDetails() {
        val movieDetails =
            loadJSONFromResourcesFile(Filenames.MOVIE_DETAILS_FILENAME, MovieResponse::class.java)
        assertThat(movieDetails).isNotNull()
        assertThat(movieDetails).isEqualTo(
            MovieResponse(
                id = "436969",
                imdbId = "tt6334354",
                adult = false,
                genres = listOf(
                    GenreDto(id = "28", "Acción"),
                    GenreDto(id = "12", "Aventura"),
                    GenreDto(id = "14", "Fantasía"),
                    GenreDto(id = "35", "Comedia")
                ),
                homepageUrl = "https://www.thesuicidesquad.net",
                backdropPath = "/jlGmlFOcfo8n5tURmhC7YVd4Iyy.jpg",
                posterPath = "/fPJWlhXA2VXf4MlQ3JenVsz1iba.jpg",
                originalTitle = "The Suicide Squad",
                title = "El Escuadrón Suicida",
                originalLanguage = "en",
                overview = "Un grupo de super villanos se encuentran encerrados en Belle Reve, una prisión de alta seguridad con la tasa de mortalidad más alta de Estados Unidos. Para salir de allí harán cualquier cosa, incluso unirse al grupo Task Force X, dedicado a llevar a cabo misiones suicidas bajo las órdenes de Amanda Waller. Fuertemente armados son enviados a la isla Corto Maltese, una jungla repleta de enemigos.",
                releaseDate = "2021-07-28",
                voteAverage = 8f,
                runtime = 132,
                status = "Released",
                tagline = "Se mueren por salvar el mundo."
            )
        )
    }

    @Test
    fun load_moviesList() {
        val moviesList = loadJSONFromResourcesFile(Filenames.MOVIES_LIST_FILENAME, MoviesListResponse::class.java)
        assertThat(moviesList).isNotNull()
        assertThat(moviesList.results).isNotEmpty()
        assertThat(moviesList.results).contains(
            MovieListItemDto(
                id = "436969",
                adult = false,
                genreIds = listOf("28", "12", "14", "35"),
                backdropPath = "/jlGmlFOcfo8n5tURmhC7YVd4Iyy.jpg",
                posterPath = "/fPJWlhXA2VXf4MlQ3JenVsz1iba.jpg",
                originalTitle = "The Suicide Squad",
                title = "El Escuadrón Suicida",
                originalLanguage = "en",
                overview = "Un grupo de super villanos se encuentran encerrados en Belle Reve, una prisión de alta seguridad con la tasa de mortalidad más alta de Estados Unidos. Para salir de allí harán cualquier cosa, incluso unirse al grupo Task Force X, dedicado a llevar a cabo misiones suicidas bajo las órdenes de Amanda Waller. Fuertemente armados son enviados a la isla Corto Maltese, una jungla repleta de enemigos.",
                releaseDate = "2021-07-28",
                voteAverage = 8f
            )
        )
    }

    @Test
    fun load_peopleList() {
        val peopleList = loadJSONFromResourcesFile(Filenames.PEOPLE_LIST_FILENAME, PersonListResponse::class.java)
        assertThat(peopleList).isNotNull()
        assertThat(peopleList.results).isNotEmpty()
        assertThat(peopleList.results).contains(
            PersonListItemDto(
                id = "192",
                name = "Morgan Freeman",
                profilePath = "/oIciQWr8VwKoR8TmAw1owaiZFyb.jpg",
                knownForDepartment = "Acting",
                knownFor = listOf(
                    MovieListItemDto(
                        id = "155",
                        adult = false,
                        genreIds = listOf("18", "28", "80", "53"),
                        backdropPath = "/nMKdUUepR0i5zn0y1T4CsSB5chy.jpg",
                        posterPath = "/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
                        originalTitle = "The Dark Knight",
                        title = "The Dark Knight",
                        originalLanguage = "en",
                        overview = "Batman raises the stakes in his war on crime. With the help of Lt. Jim Gordon and District Attorney Harvey Dent, Batman sets out to dismantle the remaining criminal organizations that plague the streets. The partnership proves to be effective, but they soon find themselves prey to a reign of chaos unleashed by a rising criminal mastermind known to the terrified citizens of Gotham as the Joker.",
                        releaseDate = "2008-07-16",
                        voteAverage = 8.5f
                    ),
                    MovieListItemDto(
                        id = "278",
                        adult = false,
                        genreIds = listOf("18", "80"),
                        backdropPath = "/9Xw0I5RV2ZqNLpul6lXKoviYg55.jpg",
                        posterPath = "/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
                        originalTitle = "The Shawshank Redemption",
                        title = "The Shawshank Redemption",
                        originalLanguage = "en",
                        overview = "Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills to work for an amoral warden. During his long stretch in prison, Dufresne comes to be admired by the other inmates -- including an older prisoner named Red -- for his integrity and unquenchable sense of hope.",
                        releaseDate = "1994-09-23",
                        voteAverage = 8.7f
                    ),
                    MovieListItemDto(
                        id = "49026",
                        adult = false,
                        genreIds = listOf("28", "80", "18", "53"),
                        backdropPath = "/cKPfiu9IcCW0fMdKdQBXe3PRtTZ.jpg",
                        posterPath = "/vzvKcPQ4o7TjWeGIn0aGC9FeVNu.jpg",
                        originalTitle = "The Dark Knight Rises",
                        title = "The Dark Knight Rises",
                        originalLanguage = "en",
                        overview = "Following the death of District Attorney Harvey Dent, Batman assumes responsibility for Dent's crimes to protect the late attorney's reputation and is subsequently hunted by the Gotham City Police Department. Eight years later, Batman encounters the mysterious Selina Kyle and the villainous Bane, a new terrorist leader who overwhelms Gotham's finest. The Dark Knight resurfaces to protect a city that has branded him an enemy.",
                        releaseDate = "2012-07-16",
                        voteAverage = 7.8f
                    )
                )
            )
        )
    }


}