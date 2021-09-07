package com.diegoparra.kino.data

import com.diegoparra.kino.data.local.FakeFavouritesDao
import com.diegoparra.kino.data.local.FavouritesDao
import com.diegoparra.kino.data.network.FakeDtoData
import com.diegoparra.kino.data.network.MoviesApi
import com.diegoparra.kino.data.network.MoviesDtoMappers
import com.diegoparra.kino.data.network.MoviesDtoMappersImpl
import com.diegoparra.kino.data.network.dtos.GenresResponse
import com.diegoparra.kino.data.network.dtos.MoviesListResponse
import com.diegoparra.kino.data.network.dtos.PersonListResponse
import com.diegoparra.kino.models.Genre
import com.diegoparra.kino.models.Movie
import com.diegoparra.kino.models.People
import com.diegoparra.kino.utils.Either
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MoviesRepositoryImplTest {

    /*  // Can be used instead of @RunWith(MockitoJUnitRunner::class)
        @get:Rule val mockitoRule = MockitoJUnit.rule() */

    @Mock
    private lateinit var moviesApi: MoviesApi

    private lateinit var favouritesDao: FavouritesDao
    private lateinit var moviesRepositoryImpl: MoviesRepositoryImpl

    private val coroutineDispatcher = TestCoroutineDispatcher()

    //  This does not save any state, so it is safe to call here rather than in setUp method
    private val moviesDtoMappers: MoviesDtoMappers = MoviesDtoMappersImpl()


    //  Sample data to work in most of the methods, where some movieListResponse is returned
    private val emptyMoviesListReponse = MoviesListResponse(1, emptyList(), 50, 1000)
    private val moviesListResponse = MoviesListResponse(
        1,
        listOf(
            FakeDtoData.Movies.suicideSquad.movieListItemDto,
            FakeDtoData.Movies.jungleCruise.movieListItemDto
        ),
        50,
        1000
    )
    private val parsedMoviesListResponse = moviesDtoMappers.toMoviesList(moviesListResponse)


    @Before
    fun setUp() {
        favouritesDao = FakeFavouritesDao()
        moviesRepositoryImpl =
            MoviesRepositoryImpl(moviesApi, moviesDtoMappers, favouritesDao, coroutineDispatcher)
    }

    @Test
    fun getGenres_success_returnEitherRightGenresList() = coroutineDispatcher.runBlockingTest {
        val genre1 = FakeDtoData.Genres.action
        val genre2 = FakeDtoData.Genres.comedy
        val genresResponse = GenresResponse(listOf(genre1, genre2))
        val parsedGenresResponse = moviesDtoMappers.toGenreList(genresResponse)

        Mockito.`when`(moviesApi.getGenres()).thenReturn(genresResponse)
        val result = moviesRepositoryImpl.getGenres()
        Mockito.verify(moviesApi).getGenres()

        assertThat(result).isEqualTo(Either.Right(parsedGenresResponse))
        assertThat(result.getOrNull()).hasSize(genresResponse.genres.size)
    }

    @Test
    fun getGenres_emptyList_returnEitherRightEmptyList() = coroutineDispatcher.runBlockingTest {
        Mockito.`when`(moviesApi.getGenres()).thenReturn(GenresResponse(emptyList()))
        val result = moviesRepositoryImpl.getGenres()
        Mockito.verify(moviesApi).getGenres()

        assertThat(result).isInstanceOf(Either.Right::class.java)
        assertThat(result).isEqualTo(Either.Right(emptyList<Genre>()))
    }

    @Test
    fun getGenres_error_returnEitherLeft() = coroutineDispatcher.runBlockingTest {
        val exception = NullPointerException()

        Mockito.`when`(moviesApi.getGenres()).thenThrow(exception)
        val result = moviesRepositoryImpl.getGenres()
        Mockito.verify(moviesApi).getGenres()

        assertThat(result).isEqualTo(Either.Left(exception))
    }


    @Test
    fun getMoviesByGenre_success_returnEitherRightMoviesList() =
        coroutineDispatcher.runBlockingTest {
            val genreId = "28"

            Mockito.`when`(moviesApi.getMoviesByGenre(genreId)).thenReturn(moviesListResponse)
            val result = moviesRepositoryImpl.getMoviesByGenre(genreId)
            Mockito.verify(moviesApi).getMoviesByGenre(genreId)

            assertThat(result).isEqualTo(Either.Right(parsedMoviesListResponse))
            assertThat(result.getOrNull()).hasSize(moviesListResponse.results.size)
        }

    @Test
    fun getMoviesByGenre_emptyList_returnEitherRightEmptyList() =
        coroutineDispatcher.runBlockingTest {
            val genreId = "28"

            Mockito.`when`(moviesApi.getMoviesByGenre(genreId)).thenReturn(emptyMoviesListReponse)
            val result = moviesRepositoryImpl.getMoviesByGenre(genreId)
            Mockito.verify(moviesApi).getMoviesByGenre(genreId)

            assertThat(result).isInstanceOf(Either.Right::class.java)
            assertThat(result).isEqualTo(Either.Right(emptyList<Movie>()))
        }

    @Test
    fun getMoviesByGenre_error_returnEitherLeft() = coroutineDispatcher.runBlockingTest {
        val genreId = "28"
        val exception = NullPointerException()

        Mockito.`when`(moviesApi.getMoviesByGenre(genreId)).thenThrow(exception)
        val result = moviesRepositoryImpl.getMoviesByGenre(genreId)
        Mockito.verify(moviesApi).getMoviesByGenre(genreId)

        assertThat(result).isEqualTo(Either.Left(exception))
    }


    @Test
    fun getMovieById_success_returnEitherRightMovie() = coroutineDispatcher.runBlockingTest {
        val movie = FakeDtoData.Movies.suicideSquad.movieResponse
        val movieId = movie.id
        val parsedMovie = moviesDtoMappers.toMovie(movie)

        Mockito.`when`(moviesApi.getMovieById(movieId)).thenReturn(movie)
        val result = moviesRepositoryImpl.getMovieById(movieId)
        Mockito.verify(moviesApi).getMovieById(movieId)

        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(Either.Right(parsedMovie))
    }

    @Test
    fun getMovieById_error_returnEitherLeft() = coroutineDispatcher.runBlockingTest {
        val movieId = "436969"
        val exception = NullPointerException()

        Mockito.`when`(moviesApi.getMovieById(movieId)).thenThrow(exception)
        val result = moviesRepositoryImpl.getMovieById(movieId)
        Mockito.verify(moviesApi).getMovieById(movieId)

        assertThat(result).isEqualTo(Either.Left(exception))
    }


    @Test
    fun toggleFavourite_isNotFavourite_returnFavouriteTrue() = coroutineDispatcher.runBlockingTest {
        val movieId = "436969"
        assertThat(favouritesDao.isFavourite(movieId)).isFalse()

        moviesRepositoryImpl.toggleFavourite(movieId)

        assertThat(favouritesDao.isFavourite(movieId)).isTrue()
    }

    @Test
    fun toggleFavourite_isFavourite_returnFavouriteFalse() = coroutineDispatcher.runBlockingTest {
        val movieId = "436969"
        favouritesDao.addFavourite(movieId)
        assertThat(favouritesDao.isFavourite(movieId)).isTrue()

        moviesRepositoryImpl.toggleFavourite(movieId)

        assertThat(favouritesDao.isFavourite(movieId)).isFalse()
    }

    @Test
    fun toggleFavourite_pressedTwice_returnOriginalFavouriteState() =
        coroutineDispatcher.runBlockingTest {
            val movieId = "436969"
            val originalState = favouritesDao.isFavourite(movieId)

            moviesRepositoryImpl.toggleFavourite(movieId)
            moviesRepositoryImpl.toggleFavourite(movieId)

            assertThat(favouritesDao.isFavourite(movieId)).isEqualTo(originalState)
        }


    @Test
    fun isFavourite_isFavouriteInDatabase_returnEitherRightTrue() =
        coroutineDispatcher.runBlockingTest {
            val movieId = "436969"
            favouritesDao.addFavourite(movieId)
            assertThat(favouritesDao.isFavourite(movieId)).isTrue()

            val flowResult = moviesRepositoryImpl.isFavourite(movieId)
            val firstResult = flowResult.first()

            assertThat(firstResult).isInstanceOf(Either.Right::class.java)
            assertThat(firstResult.getOrNull()).isTrue()
        }

    @Test
    fun isFavourite_isNotFavouriteInDatabase_returnEitherRightFalse() =
        coroutineDispatcher.runBlockingTest {
            val movieId = "436969"
            assertThat(favouritesDao.isFavourite(movieId)).isFalse()

            val flowResult = moviesRepositoryImpl.isFavourite(movieId)
            val firstResult = flowResult.first()

            assertThat(firstResult).isInstanceOf(Either.Right::class.java)
            assertThat(firstResult.getOrNull()).isFalse()
        }

    @Test
    fun isFavourite_toggleFavouriteFromFalseToTrueWhileObservingIsFavourite_returnFirstFalseAndSecondTrue() =
        coroutineDispatcher.runBlockingTest {
            val movieId = "436969"
            assertThat(favouritesDao.isFavourite(movieId)).isFalse()

            val flowResult = moviesRepositoryImpl.isFavourite(movieId)
            val firstResult = flowResult.first()
            moviesRepositoryImpl.toggleFavourite(movieId)
            val secondResult = flowResult.first()

            assertThat(firstResult.getOrNull()).isFalse()
            assertThat(secondResult.getOrNull()).isTrue()
        }

    @Test
    fun getFavourites_notAddedFavourites_returnEmptyList() = coroutineDispatcher.runBlockingTest {
        val flowFavourites = favouritesDao.observeFavourites()
        assertThat(flowFavourites.first()).isEmpty()

        val flowResult = moviesRepositoryImpl.getFavourites()
        val firstResult = flowResult.first()

        assertThat(firstResult).isInstanceOf(Either.Right::class.java)
        assertThat(firstResult).isEqualTo(Either.Right(emptyList<String>()))
    }

    @Test
    fun getFavourites_preloadedFavourites_returnListWithFavourites() =
        coroutineDispatcher.runBlockingTest {
            val movie1 = FakeDtoData.Movies.suicideSquad.movieResponse
            val movie2 = FakeDtoData.Movies.jungleCruise.movieResponse
            val favourite1 = movie1.id
            val favourite2 = movie2.id
            val parsedMovie1 = moviesDtoMappers.toMovie(movie1)
            val parsedMovie2 = moviesDtoMappers.toMovie(movie2)

            favouritesDao.addFavourite(favourite1)
            favouritesDao.addFavourite(favourite2)
            val favourites = favouritesDao.observeFavourites().first()
            assertThat(favourites).containsExactly(favourite1, favourite2)

            Mockito.`when`(moviesApi.getMovieById(favourite1)).thenReturn(movie1)
            Mockito.`when`(moviesApi.getMovieById(favourite2)).thenReturn(movie2)

            val flowResult = moviesRepositoryImpl.getFavourites()
            val firstResult = flowResult.first()

            Mockito.verify(moviesApi).getMovieById(favourite1)
            Mockito.verify(moviesApi).getMovieById(favourite2)

            assertThat(firstResult).isInstanceOf(Either.Right::class.java)
            assertThat(firstResult).isEqualTo(Either.Right(listOf(parsedMovie1, parsedMovie2)))
        }

    @Test
    fun getFavourites_addedAndRemovedFavouriteWhileObserving_returnCorrectList() =
        coroutineDispatcher.runBlockingTest {
            val movie1 = FakeDtoData.Movies.suicideSquad.movieResponse
            val movie2 = FakeDtoData.Movies.jungleCruise.movieResponse
            val favourite1 = movie1.id
            val favourite2 = movie2.id
            val parsedMovie1 = moviesDtoMappers.toMovie(movie1)
            val parsedMovie2 = moviesDtoMappers.toMovie(movie2)

            favouritesDao.addFavourite(favourite1)
            val favourites = favouritesDao.observeFavourites().first()
            assertThat(favourites).containsExactly(favourite1)


            val results = mutableListOf<Either<Exception, List<Movie>>>()
            Mockito.`when`(moviesApi.getMovieById(favourite1)).thenReturn(movie1)
            Mockito.`when`(moviesApi.getMovieById(favourite2)).thenReturn(movie2)

            val flowResult = moviesRepositoryImpl.getFavourites()
            results.add(flowResult.first())

            favouritesDao.addFavourite(favourite2)
            results.add(flowResult.first())

            favouritesDao.removeFavourite(favourite1)
            results.add(flowResult.first())


            assertThat(results[0]).isEqualTo(Either.Right(listOf(parsedMovie1)))
            assertThat(results[1]).isEqualTo(Either.Right(listOf(parsedMovie1, parsedMovie2)))
            assertThat(results[2]).isEqualTo(Either.Right(listOf(parsedMovie2)))
        }


    @Test
    fun getCredits_success_returnEitherRightCredits() = coroutineDispatcher.runBlockingTest {
        val creditsResponse = FakeDtoData.Movies.suicideSquad.credits
        val movieId = creditsResponse.movieId
        val parsedCreditsResponse = moviesDtoMappers.toMovieCredits(creditsResponse)

        Mockito.`when`(moviesApi.getCredits(movieId)).thenReturn(creditsResponse)
        val result = moviesRepositoryImpl.getCredits(movieId)
        Mockito.verify(moviesApi).getCredits(movieId)

        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(Either.Right(parsedCreditsResponse))
    }

    @Test
    fun getCredits_error_returnEitherLeft() = coroutineDispatcher.runBlockingTest {
        val movieId = "436969"
        val exception = NullPointerException()

        Mockito.`when`(moviesApi.getCredits(movieId)).thenThrow(exception)
        val result = moviesRepositoryImpl.getCredits(movieId)
        Mockito.verify(moviesApi).getCredits(movieId)

        assertThat(result).isEqualTo(Either.Left(exception))
    }


    @Test
    fun getSuggestions_success_returnEitherRightMoviesList() = coroutineDispatcher.runBlockingTest {
        val movieId = "436969"

        Mockito.`when`(moviesApi.getSuggestions(movieId)).thenReturn(moviesListResponse)
        val result = moviesRepositoryImpl.getSuggestions(movieId)
        Mockito.verify(moviesApi).getSuggestions(movieId)

        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(Either.Right(parsedMoviesListResponse))
    }

    @Test
    fun getSuggestions_emptyList_returnEitherRightEmptyList() =
        coroutineDispatcher.runBlockingTest {
            val movieId = "436969"

            Mockito.`when`(moviesApi.getSuggestions(movieId)).thenReturn(emptyMoviesListReponse)
            val result = moviesRepositoryImpl.getSuggestions(movieId)
            Mockito.verify(moviesApi).getSuggestions(movieId)

            assertThat(result).isInstanceOf(Either.Right::class.java)
            assertThat(result).isEqualTo(Either.Right(emptyList<Movie>()))
        }

    @Test
    fun getSuggestions_error_returnEitherLeft() = coroutineDispatcher.runBlockingTest {
        val movieId = "436969"
        val exception = NullPointerException()

        Mockito.`when`(moviesApi.getSuggestions(movieId)).thenThrow(exception)
        val result = moviesRepositoryImpl.getSuggestions(movieId)
        Mockito.verify(moviesApi).getSuggestions(movieId)

        assertThat(result).isEqualTo(Either.Left(exception))
    }


    @Test
    fun searchMovieByName_success_returnEitherRightMoviesList() =
        coroutineDispatcher.runBlockingTest {
            val title = "suic"

            Mockito.`when`(moviesApi.searchMovieByName(title)).thenReturn(moviesListResponse)
            val result = moviesRepositoryImpl.searchMovieByName(title)
            Mockito.verify(moviesApi).searchMovieByName(title)

            assertThat(result).isNotNull()
            assertThat(result).isEqualTo(Either.Right(parsedMoviesListResponse))
        }

    @Test
    fun searchMovieByName_emptyList_returnEitherRightEmptyList() =
        coroutineDispatcher.runBlockingTest {
            val title = "abcde"

            Mockito.`when`(moviesApi.searchMovieByName(title)).thenReturn(emptyMoviesListReponse)
            val result = moviesRepositoryImpl.searchMovieByName(title)
            Mockito.verify(moviesApi).searchMovieByName(title)

            assertThat(result).isInstanceOf(Either.Right::class.java)
            assertThat(result).isEqualTo(Either.Right(emptyList<Movie>()))
        }

    @Test
    fun searchMovieByName_error_returnEitherLeft() = coroutineDispatcher.runBlockingTest {
        val title = "abcde"
        val exception = NullPointerException()

        Mockito.`when`(moviesApi.searchMovieByName(title)).thenThrow(exception)
        val result = moviesRepositoryImpl.searchMovieByName(title)
        Mockito.verify(moviesApi).searchMovieByName(title)

        assertThat(result).isEqualTo(Either.Left(exception))
    }


    @Test
    fun searchPeopleByName_success_returnEitherRightMoviesList() =
        coroutineDispatcher.runBlockingTest {
            val name = "margot robbie"
            val peopleListResponse = PersonListResponse(
                1,
                listOf(FakeDtoData.People.margotRobbie, FakeDtoData.People.tomCruise),
                50,
                1000
            )
            val parsedPeopleListResponse = moviesDtoMappers.toPeopleList(peopleListResponse)

            Mockito.`when`(moviesApi.searchPeopleByName(name)).thenReturn(peopleListResponse)
            val result = moviesRepositoryImpl.searchPeopleByName(name)
            Mockito.verify(moviesApi).searchPeopleByName(name)

            assertThat(result).isNotNull()
            assertThat(result).isEqualTo(Either.Right(parsedPeopleListResponse))
        }

    @Test
    fun searchPeopleByName_emptyList_returnEitherRightEmptyList() =
        coroutineDispatcher.runBlockingTest {
            val name = "abcde"
            val emptyPeopleListResponse = PersonListResponse(1, emptyList(), 50, 1000)

            Mockito.`when`(moviesApi.searchPeopleByName(name)).thenReturn(emptyPeopleListResponse)
            val result = moviesRepositoryImpl.searchPeopleByName(name)
            Mockito.verify(moviesApi).searchPeopleByName(name)

            assertThat(result).isInstanceOf(Either.Right::class.java)
            assertThat(result).isEqualTo(Either.Right(emptyList<People>()))
        }

    @Test
    fun searchPeopleByName_error_returnEitherLeft() = coroutineDispatcher.runBlockingTest {
        val name = "abcde"
        val exception = NullPointerException()

        Mockito.`when`(moviesApi.searchPeopleByName(name)).thenThrow(exception)
        val result = moviesRepositoryImpl.searchPeopleByName(name)
        Mockito.verify(moviesApi).searchPeopleByName(name)

        assertThat(result).isEqualTo(Either.Left(exception))
    }


    @Test
    fun searchMovieByActorId_success_returnEitherRightMoviesList() =
        coroutineDispatcher.runBlockingTest {
            val actorId = "123"

            Mockito.`when`(moviesApi.searchMovieByActorId(actorId)).thenReturn(moviesListResponse)
            val result = moviesRepositoryImpl.searchMovieByActorId(actorId)
            Mockito.verify(moviesApi).searchMovieByActorId(actorId)

            assertThat(result).isNotNull()
            assertThat(result).isEqualTo(Either.Right(parsedMoviesListResponse))
        }

    @Test
    fun searchMovieByActorId_emptyList_returnEitherRightEmptyList() =
        coroutineDispatcher.runBlockingTest {
            val actorId = "123"

            Mockito.`when`(moviesApi.searchMovieByActorId(actorId))
                .thenReturn(emptyMoviesListReponse)
            val result = moviesRepositoryImpl.searchMovieByActorId(actorId)
            Mockito.verify(moviesApi).searchMovieByActorId(actorId)

            assertThat(result).isInstanceOf(Either.Right::class.java)
            assertThat(result).isEqualTo(Either.Right(emptyList<Movie>()))
        }

    @Test
    fun searchMovieByActorId_error_returnEitherLeft() = coroutineDispatcher.runBlockingTest {
        val actorId = "123"
        val exception = NullPointerException()

        Mockito.`when`(moviesApi.searchMovieByActorId(actorId)).thenThrow(exception)
        val result = moviesRepositoryImpl.searchMovieByActorId(actorId)
        Mockito.verify(moviesApi).searchMovieByActorId(actorId)

        assertThat(result).isEqualTo(Either.Left(exception))
    }

}