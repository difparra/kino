package com.diegoparra.kino.data

import com.diegoparra.kino.models.Genre
import com.diegoparra.kino.models.Movie
import com.diegoparra.kino.utils.Either
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    suspend fun getGenres(): Either<Exception, List<Genre>>
    suspend fun getMoviesByGenre(genreId: String): Either<Exception, List<Movie>>
    suspend fun getMovieById(id: String): Either<Exception, Movie>

    suspend fun toggleFavourite(movieId: String)
    fun isFavourite(movieId: String): Flow<Either<Exception, Boolean>>
    fun getFavourites(): Flow<Either<Exception, List<Movie>>>

}