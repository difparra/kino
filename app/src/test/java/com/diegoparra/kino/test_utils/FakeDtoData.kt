package com.diegoparra.kino.test_utils

import com.diegoparra.kino.data.network.dtos.*

object FakeDtoData {

    object Genres {
        val action = GenreDto(id = "28", name = "Acción")
        val adventure = GenreDto(id = "12", name = "Aventura")
        val comedy = GenreDto(id = "135", name = "Comedia")
        val fantasy = GenreDto(id = "14", name = "Fantasía")
        val scienceFiction = GenreDto(id = "878", name = "Ciencia ficción")
    }

    object Movies {
        val suicideSquad = FakeMovieDto(
            movieResponse = MovieResponse(
                id = "436969",
                imdbId = "tt6334354",
                adult = false,
                genres = listOf(Genres.action, Genres.adventure, Genres.fantasy, Genres.comedy),
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
            ),
            cast = listOf(
                CastDto(
                    id = "234352",
                    knownForDepartment = "Acting",
                    name = "Margot Robbie",
                    profilePath = "/euDPyqLnuwaWMHajcU3oZ9uZezR.jpg",
                    character = "Harleen Quinzel / Harley Quinn"
                ),
                CastDto(
                    id = "17605",
                    knownForDepartment = "Acting",
                    name = "Idris Elba",
                    profilePath = "/be1bVF7qGX91a6c5WeRPs5pKXln.jpg",
                    character = "Robert DuBois / Bloodsport"
                )
            ),
            producer = CrewDto(
                id = "282",
                knownForDepartment = "Production",
                name = "Charles Roven",
                profilePath = "/4uJLoVstC1CBcArXFOe53N2fDr1.jpg",
                job = "Producer"
            )
        )

        val jungleCruise = FakeMovieDto(
            movieResponse = MovieResponse(
                id = "451048",
                imdbId = "tt0870154",
                adult = false,
                genres = listOf(Genres.adventure, Genres.fantasy, Genres.comedy, Genres.action),
                homepageUrl = "https://movies.disney.com/jungle-cruise",
                backdropPath = "/7WJjFviFBffEJvkAms4uWwbcVUk.jpg",
                posterPath = "/esgrxpxrQufea2A3iHJUquG4mdz.jpg",
                originalTitle = "Jungle Cruise",
                title = "Jungle Cruise",
                originalLanguage = "en",
                overview = "Principios del siglo XX. Frank es el carismático capitán de una peculiar embarcación que recorre la selva amazónica. Allí, a pesar de los peligros que el río Amazonas les tiene preparados, Frank llevará en su barco a la científica Lily Houghton y a su hermano McGregor Houghton. Su misión será encontrar un árbol místico que podría tener poderes curativos. Claro que su objetivo no será fácil, y en su aventura se encontrarán con toda clase de dificultades, además de una expedición alemana que busca también este árbol con propiedades curativas. Esta comedia de acción y aventuras está basada en la atracción Jungle Cruise de los parques de ocio de Disney.",
                releaseDate = "2021-07-28",
                voteAverage = 7.9f,
                runtime = 127,
                status = "Released",
                tagline = "El mito es real.",
            ),
            cast = listOf(
                CastDto(
                    id = "18918",
                    knownForDepartment = "Acting",
                    name = "Dwayne Johnson",
                    profilePath = "/cgoy7t5Ve075naBPcewZrc08qGw.jpg",
                    character = "Frank Wolff",
                ),
                CastDto(
                    id = "5081",
                    knownForDepartment = "Acting",
                    name = "Emily Blunt",
                    profilePath = "/jqlqq3knztTnty5rcMg5evqZRCa.jpg",
                    character = "Dr. Lily Houghton"
                )
            ),
            producer = CrewDto(
                id = "2043",
                knownForDepartment = "Production",
                name = "John Davis",
                profilePath = "/kpXOBMUBZe8XpHYk7s0721kjjIk.jpg",
                job = "Producer"
            )
        )

        val theTomorrowWar = FakeMovieDto(
            movieResponse = MovieResponse(
                id = "588228",
                imdbId = "tt9777666",
                adult = false,
                genres = listOf(Genres.action, Genres.scienceFiction, Genres.adventure),
                homepageUrl = "https://www.amazon.com/dp/B093CQZ2SM",
                backdropPath = "/yizL4cEKsVvl17Wc1mGEIrQtM2F.jpg",
                posterPath = "/tzp6VzED2TBc02bkYoYnQa6r2OK.jpg",
                originalTitle = "The Tomorrow War",
                title = "La guerra del mañana",
                originalLanguage = "en",
                overview = "Un día el mundo se paraliza cuando un grupo de viajeros en el tiempo se transporta desde el año 2051 para entregar un mensaje urgente: La humanidad está perdiendo la guerra mundial contra una especie alienígena letal. La única esperanza de supervivencia es que los soldados y los ciudadanos del presente sean transportados al futuro y se unan a la lucha. Entre los reclutados está el profesor de instituto y padre de familia Dan Forester (Chris Pratt) quien, decidido a salvar el mundo por su hija, se une a una brillante científica (Yvonne Strahovski) y a su padre (J.K. Simmons) en una búsqueda desesperada por reescribir el destino del planeta.",
                releaseDate = "2021-09-03",
                voteAverage = 7.8f,
                runtime = 140,
                status = "Released",
                tagline = "La lucha por el mañana comienza hoy."
            ),
            cast = listOf(
                CastDto(
                    id = "73457",
                    knownForDepartment = "Acting",
                    name = "Chris Pratt",
                    profilePath = "/gXKyT1YU5RWWPaE1je3ht58eUZr.jpg",
                    character = "Dan Forester"
                ),
                CastDto(
                    id = "63312",
                    knownForDepartment = "Acting",
                    name = "Yvonne Strahovski",
                    profilePath = "/wio1VaQDOggDfPOTJf2vxGfooxZ.jpg",
                    character = "Romeo Command"
                )
            ),
            producer = CrewDto(
                id = "3893",
                knownForDepartment = "Production",
                name = "David S. Goyer",
                profilePath = "/gf44Hr3HJuWK7ZMHQKzDNBe0ylI.jpg",
                job = "Producer"
            )
        )

    }

    object People {
        val morganFreeman = PersonListItemDto(
            id = "192",
            name = "Morgan Freeman",
            profilePath = "/oIciQWr8VwKoR8TmAw1owaiZFyb.jpg",
            knownForDepartment = "Acting",
            knownFor = listOf(Movies.suicideSquad.movieListItemDto)
        )
        val margotRobbie = PersonListItemDto(
            id = "234352",
            name = "Margot Robbie",
            profilePath = "/euDPyqLnuwaWMHajcU3oZ9uZezR.jpg",
            knownForDepartment = "Acting",
            knownFor = listOf(Movies.suicideSquad.movieListItemDto)
        )
        val tomCruise = PersonListItemDto(
            id = "500",
            name = "Tom Cruise",
            profilePath = "/8qBylBsQf4llkGrWR3qAsOtOU8O.jpg",
            knownForDepartment = "Acting",
            knownFor = listOf(
                Movies.jungleCruise.movieListItemDto,
                Movies.theTomorrowWar.movieListItemDto
            )
        )
    }

}



class FakeMovieDto(
    val movieResponse: MovieResponse,
    private val cast: List<CastDto>,
    private val producer: CrewDto
) {
    val movieListItemDto = with(movieResponse) {
        MovieListItemDto(
            id = id,
            adult = adult,
            genreIds = genres?.map { it.id },
            backdropPath = backdropPath,
            posterPath = posterPath,
            originalTitle = originalTitle,
            title = title,
            originalLanguage = originalLanguage,
            overview = overview,
            releaseDate = releaseDate,
            voteAverage = voteAverage
        )
    }

    val credits = CreditsResponse(
        movieId = movieResponse.id,
        cast = cast,
        crew = listOf(producer)
    )

}