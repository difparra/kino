package com.diegoparra.kino.data

import com.diegoparra.kino.data.network.DtoTransformations.toGenreList
import com.diegoparra.kino.data.network.DtoTransformations.toMovie
import com.diegoparra.kino.data.network.DtoTransformations.toMoviesList
import com.diegoparra.kino.data.network.MoviesApi
import com.diegoparra.kino.di.IoDispatcher
import com.diegoparra.kino.models.Genre
import com.diegoparra.kino.models.Movie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val api: MoviesApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : MoviesRepository {

    override suspend fun getGenres(): Result<List<Genre>> = withContext(dispatcher) {
        kotlin.runCatching {
            api.getGenres().toGenreList()
        }
    }

    override suspend fun getMoviesByGenre(genreId: String): Result<List<Movie>> =
        withContext(dispatcher) {
            kotlin.runCatching {
                api.getMoviesByGenre(genreId).toMoviesList()
            }
        }

    override suspend fun getMovieById(id: String): Result<Movie> = withContext(dispatcher) {
        kotlin.runCatching {
            api.getMovieById(id).toMovie()
        }
    }

}