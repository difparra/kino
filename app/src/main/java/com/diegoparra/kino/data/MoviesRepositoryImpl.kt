package com.diegoparra.kino.data

import com.diegoparra.kino.data.local.FavouritesDao
import com.diegoparra.kino.data.network.MoviesApi
import com.diegoparra.kino.data.network.MoviesDtoMappers
import com.diegoparra.kino.di.IoDispatcher
import com.diegoparra.kino.models.Genre
import com.diegoparra.kino.models.Movie
import com.diegoparra.kino.models.MovieCredits
import com.diegoparra.kino.models.People
import com.diegoparra.kino.utils.Either
import com.diegoparra.kino.utils.reduceFailuresOrRight
import com.diegoparra.kino.utils.runCatching
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesApi: MoviesApi,
    private val moviesDtoMappers: MoviesDtoMappers,
    private val favouritesDao: FavouritesDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : MoviesRepository {

    override suspend fun getGenres(): Either<Exception, List<Genre>> = withContext(dispatcher) {
        Either.runCatching {
            moviesDtoMappers.toGenreList(moviesApi.getGenres())
        }
    }

    override suspend fun getMoviesByGenre(genreId: String): Either<Exception, List<Movie>> =
        withContext(dispatcher) {
            Either.runCatching {
                moviesDtoMappers.toMoviesList(moviesApi.getMoviesByGenre(genreId))
            }
        }

    override suspend fun getMovieById(id: String): Either<Exception, Movie> =
        withContext(dispatcher) {
            Either.runCatching {
                moviesDtoMappers.toMovie(moviesApi.getMovieById(id))
            }
        }

    override suspend fun toggleFavourite(movieId: String) = withContext(dispatcher) {
        favouritesDao.isFavourite(movieId).let { isFavourite ->
            if (isFavourite) {
                favouritesDao.removeFavourite(movieId)
            } else {
                favouritesDao.addFavourite(movieId)
            }
        }
    }

    override fun isFavourite(movieId: String): Flow<Either<Exception, Boolean>> {
        return favouritesDao.observeIsFavourite(movieId)
            .map { Either.runCatching { it } }
            .flowOn(dispatcher)
    }

    override fun getFavourites(): Flow<Either<Exception, List<Movie>>> {
        return favouritesDao.observeFavourites()
            .map {
                coroutineScope {
                    val moviesAsyncList = it.map { movieId ->
                        async { getMovieById(movieId) }
                    }
                    val movies = moviesAsyncList.awaitAll()
                    movies.reduceFailuresOrRight()
                }
            }
            .flowOn(dispatcher)
    }


    override suspend fun getCredits(movieId: String): Either<Exception, MovieCredits> =
        withContext(dispatcher) {
            Either.runCatching {
                moviesDtoMappers.toMovieCredits(moviesApi.getCredits(movieId))
            }
        }

    override suspend fun getSuggestions(movieId: String): Either<Exception, List<Movie>> =
        withContext(dispatcher) {
            Either.runCatching {
                moviesDtoMappers.toMoviesList(moviesApi.getSuggestions(movieId))
            }
        }

    override suspend fun searchMovieByName(title: String): Either<Exception, List<Movie>> =
        withContext(dispatcher) {
            Either.runCatching {
                moviesDtoMappers.toMoviesList(moviesApi.searchMovieByName(title))
            }
        }

    override suspend fun searchPeopleByName(name: String): Either<Exception, List<People>> =
        withContext(dispatcher) {
            Either.runCatching {
                moviesDtoMappers.toPeopleList(moviesApi.searchPeopleByName(name))
            }
        }

    override suspend fun searchMovieByActorId(actorId: String): Either<Exception, List<Movie>> =
        withContext(dispatcher) {
            Either.runCatching {
                moviesDtoMappers.toMoviesList(moviesApi.searchMovieByActorId(actorId))
            }
        }
}