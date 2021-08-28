package com.diegoparra.kino.data

import com.diegoparra.kino.models.Genre
import com.diegoparra.kino.models.Movie

interface MoviesRepository {

    suspend fun getGenres(): Result<List<Genre>>
    suspend fun getMoviesByGenre(genreId: String): Result<List<Movie>>
    suspend fun getMovieById(id: String): Result<Movie>

}