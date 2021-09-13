package com.diegoparra.kino.ui._utils

import com.diegoparra.kino.models.Genre
import com.diegoparra.kino.models.GenreWithMovies
import com.diegoparra.kino.models.Movie
import java.time.LocalDate

object MoviesFakes {

    val SuicideSquad = Movie(
        id = "436969",
        title = "The Suicide Squad",
        posterUrl = "https://image.tmdb.org/t/p/original/fPJWlhXA2VXf4MlQ3JenVsz1iba.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/jlGmlFOcfo8n5tURmhC7YVd4Iyy.jpg",
        overview = "Un grupo de super villanos se encuentran encerrados en Belle Reve, una prisión de alta seguridad con la tasa de mortalidad más alta de Estados Unidos. Para salir de allí harán cualquier cosa, incluso unirse al grupo Task Force X, dedicado a llevar a cabo misiones suicidas bajo las órdenes de Amanda Waller. Fuertemente armados son enviados a la isla Corto Maltese, una jungla repleta de enemigos.",
        genreIds = listOf("28", "12", "14", "35"),
        rating = 80,
        releaseDate = LocalDate.of(2021, 7, 28),
        runtimeMinutes = 132
    )

    val JungleCruise = Movie(
        id = "451048",
        title = "Jungle Cruise",
        posterUrl = "https://image.tmdb.org/t/p/original/esgrxpxrQufea2A3iHJUquG4mdz.jpg",
        backdropUrl = "https://image.tmdb.org/t/p/original/7WJjFviFBffEJvkAms4uWwbcVUk.jpg",
        overview = "Principios del siglo XX. Frank (Dwayne Johnson) es el carismático capitán de una peculiar embarcación que recorre la selva amazónica. Allí, a pesar de los peligros que el río Amazonas les tiene preparados, Frank llevará en su barco a la científica Lily Houghton (Emily Blunt) y a su hermano McGregor Houghton (Jack Whitehall). Su misión será encontrar un árbol místico que podría tener poderes curativos. Claro que su objetivo no será fácil, y en su aventura se encontrarán con toda clase de dificultades, además de una expedición alemana que busca también este árbol con propiedades curativas. Esta comedia de acción y aventuras está basada en la atracción Jungle Cruise de los parques de ocio de Disney.",
        genreIds = listOf("12", "14", "35", "28"),
        rating = 79,
        releaseDate = LocalDate.of(2021,7,28),
        runtimeMinutes = 127
    )

}

object GenresFakes {

    val actionGenre = Genre(id = "28", "Acción")
    val adventureGenre = Genre(id = "28", "Aventura")

    val genresList = listOf(actionGenre, adventureGenre)

    val genreAndMovies = listOf(
        GenreWithMovies(actionGenre, listOf(MoviesFakes.SuicideSquad, MoviesFakes.JungleCruise)),
        GenreWithMovies(adventureGenre, listOf(MoviesFakes.SuicideSquad))
    )
}