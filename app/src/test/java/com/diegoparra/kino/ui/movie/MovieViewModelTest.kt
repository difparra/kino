package com.diegoparra.kino.ui.movie

import androidx.lifecycle.SavedStateHandle
import com.diegoparra.kino.data.MoviesRepository
import com.diegoparra.kino.data.network.MoviesDtoMappersImpl
import com.diegoparra.kino.test_utils.FakeDtoData
import com.diegoparra.kino.test_utils.MainCoroutineRule
import com.diegoparra.kino.utils.Either
import com.diegoparra.kino.utils.Resource
import com.diegoparra.kino.utils.map
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var moviesRepo: MoviesRepository
    private lateinit var viewModel: MovieViewModel

    private val mapper = MoviesDtoMappersImpl()
    private val genre1 = with(mapper) { FakeDtoData.Genres.action.toGenre() }
    private val genre2 = with(mapper) { FakeDtoData.Genres.comedy.toGenre() }
    private val movie1 = with(mapper) {
        FakeDtoData.Movies.suicideSquad.movieListItemDto.toMovie()
            .copy(genreIds = listOf(genre1.id, genre2.id))
    }
    private val savedStateHandle = SavedStateHandle().apply {
        set(MovieViewModel.MOVIE_ID_KEY, movie1.id)
    }

    private fun happyPathSetUp() {
        mainCoroutineRule.runBlockingTest {
            Mockito.`when`(moviesRepo.getMovieById(movie1.id)).thenReturn(Either.Right(movie1))
            Mockito.`when`(moviesRepo.getGenres()).thenReturn(Either.Right(listOf(genre1, genre2)))
            Mockito.`when`(moviesRepo.isFavourite(movie1.id))
                .thenReturn(flow { emit(Either.Right(false)) })
        }
        viewModel = MovieViewModel(moviesRepo, savedStateHandle)
    }

    @Test
    fun movie_happyPath() = mainCoroutineRule.runBlockingTest {
        happyPathSetUp()
        assertThat(viewModel.movie.first()).isEqualTo(Resource.Success(movie1))
    }

    @Test
    fun genreNames_happyPath() = mainCoroutineRule.runBlockingTest {
        happyPathSetUp()
        assertThat(viewModel.genreNames.first()).isEqualTo(listOf(genre1.name, genre2.name))
    }

    @Test
    fun isFavourite_happyPath() = mainCoroutineRule.runBlockingTest {
        happyPathSetUp()
        assertThat(viewModel.isFavourite.first()).isFalse()
    }

    @Test
    fun movie_failureFromRepo() = mainCoroutineRule.runBlockingTest {
        val exception = NoSuchElementException()
        Mockito.`when`(moviesRepo.getMovieById(movie1.id)).thenReturn(Either.Left(exception))
        Mockito.`when`(moviesRepo.getGenres()).thenReturn(Either.Right(listOf(genre1, genre2)))
        Mockito.`when`(moviesRepo.isFavourite(movie1.id))
            .thenReturn(flow { emit(Either.Right(false)) })
        viewModel = MovieViewModel(moviesRepo, savedStateHandle)

        assertThat(viewModel.movie.first()).isEqualTo(Resource.Error(exception))
        assertThat(viewModel.genreNames.first()).isEmpty()
        assertThat(viewModel.isFavourite.first()).isFalse()
    }

    @Test
    fun genresFailureFromRepo() = mainCoroutineRule.runBlockingTest {
        val exception = NoSuchElementException()
        Mockito.`when`(moviesRepo.getMovieById(movie1.id)).thenReturn(Either.Right(movie1))
        Mockito.`when`(moviesRepo.getGenres()).thenReturn(Either.Left(exception))
        Mockito.`when`(moviesRepo.isFavourite(movie1.id))
            .thenReturn(flow { emit(Either.Right(false)) })
        viewModel = MovieViewModel(moviesRepo, savedStateHandle)

        assertThat(viewModel.movie.first()).isEqualTo(Resource.Success(movie1))
        assertThat(viewModel.genreNames.first()).isEmpty()
        assertThat(viewModel.isFavourite.first()).isFalse()
    }

    @Test
    fun isFavouriteFailureFromRepo() = mainCoroutineRule.runBlockingTest {
        val exception = NoSuchElementException()
        Mockito.`when`(moviesRepo.getMovieById(movie1.id)).thenReturn(Either.Right(movie1))
        Mockito.`when`(moviesRepo.getGenres()).thenReturn(Either.Right(listOf(genre1, genre2)))
        Mockito.`when`(moviesRepo.isFavourite(movie1.id))
            .thenReturn(flow { emit(Either.Left(exception)) })
        viewModel = MovieViewModel(moviesRepo, savedStateHandle)

        assertThat(viewModel.movie.first()).isEqualTo(Resource.Success(movie1))
        assertThat(viewModel.genreNames.first()).isEqualTo(listOf(genre1.name, genre2.name))
        assertThat(viewModel.isFavourite.first()).isFalse()
    }

    @Test
    fun toggleFavourite() = mainCoroutineRule.runBlockingTest {
        val isFavourite = MutableStateFlow<Either<Exception, Boolean>>(Either.Right(false))
        Mockito.`when`(moviesRepo.getMovieById(movie1.id)).thenReturn(Either.Right(movie1))
        Mockito.`when`(moviesRepo.getGenres()).thenReturn(Either.Right(listOf(genre1, genre2)))
        Mockito.`when`(moviesRepo.isFavourite(movie1.id)).thenReturn(isFavourite)
        Mockito.`when`(moviesRepo.toggleFavourite(movie1.id)).then {
            isFavourite.value = isFavourite.value.map { !it }
            Unit
        }
        viewModel = MovieViewModel(moviesRepo, savedStateHandle)
        assertThat(viewModel.isFavourite.first()).isFalse()

        viewModel.toggleFavourite()
        assertThat(viewModel.isFavourite.first()).isTrue()
    }

}