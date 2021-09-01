package com.diegoparra.kino.data

import com.diegoparra.kino.data.local.FavouritesDao
import com.diegoparra.kino.data.network.DtoTransformations.toGenreList
import com.diegoparra.kino.data.network.DtoTransformations.toMovie
import com.diegoparra.kino.data.network.DtoTransformations.toMoviesList
import com.diegoparra.kino.data.network.MoviesApi
import com.diegoparra.kino.di.IoDispatcher
import com.diegoparra.kino.models.Genre
import com.diegoparra.kino.models.Movie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesApi: MoviesApi,
    private val favouritesDao: FavouritesDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : MoviesRepository {

    override suspend fun getGenres(): Result<List<Genre>> = withContext(dispatcher) {
        kotlin.runCatching {
            moviesApi.getGenres().toGenreList()
        }
    }

    override suspend fun getMoviesByGenre(genreId: String): Result<List<Movie>> =
        withContext(dispatcher) {
            kotlin.runCatching {
                moviesApi.getMoviesByGenre(genreId).toMoviesList()
            }
        }

    override suspend fun getMovieById(id: String): Result<Movie> = withContext(dispatcher) {
        kotlin.runCatching {
            moviesApi.getMovieById(id).toMovie()
        }
    }


    override suspend fun addFavourite(movieId: String) = withContext(dispatcher) {
        favouritesDao.addFavourite(movieId)
    }

    override suspend fun removeFavourites(movieId: String) = withContext(dispatcher) {
        favouritesDao.removeFavourite(movieId)
    }

    override suspend fun toggleFavourite(movieId: String) = withContext(dispatcher) {
        favouritesDao.isFavouriteSingle(movieId).let { isFavourite ->
            if (isFavourite) {
                removeFavourites(movieId)
            } else {
                addFavourite(movieId)
            }
        }
    }

    override fun isFavourite(movieId: String): Flow<Result<Boolean>> {
        return favouritesDao.isFavourite(movieId)
            .map { kotlin.runCatching { it } }
            .flowOn(dispatcher)
    }

    override fun getFavourites(): Flow<Result<List<String>>> {
        return favouritesDao
            .getFavourites()
            .map { kotlin.runCatching { it } }
            .flowOn(dispatcher)
    }
}