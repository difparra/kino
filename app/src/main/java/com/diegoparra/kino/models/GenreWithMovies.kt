package com.diegoparra.kino.models

data class GenreWithMovies(
    val genre: Genre,
    val movies: List<Movie>
)