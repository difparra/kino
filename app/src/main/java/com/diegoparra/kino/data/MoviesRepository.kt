package com.diegoparra.kino.data

import com.diegoparra.kino.models.Genre
import com.diegoparra.kino.models.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    suspend fun getGenres(): Result<List<Genre>>
    suspend fun getMoviesByGenre(genreId: String): Result<List<Movie>>
    suspend fun getMovieById(id: String): Result<Movie>

    suspend fun addFavourite(movieId: String)
    suspend fun removeFavourites(movieId: String)
    suspend fun toggleFavourite(movieId: String)
    fun isFavourite(movieId: String): Flow<Result<Boolean>>
    fun getFavourites(): Flow<Result<List<String>>>

}