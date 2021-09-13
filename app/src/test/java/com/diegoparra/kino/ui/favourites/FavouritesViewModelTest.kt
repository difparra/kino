package com.diegoparra.kino.ui.favourites

import com.diegoparra.kino.data.MoviesRepository
import com.diegoparra.kino.data.network.MoviesDtoMappersImpl
import com.diegoparra.kino.test_utils.FakeDtoData
import com.diegoparra.kino.test_utils.MainCoroutineRule
import com.diegoparra.kino.utils.Either
import com.diegoparra.kino.utils.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FavouritesViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var moviesRepo: MoviesRepository
    private lateinit var viewModel: FavouritesViewModel

    private val mapper = MoviesDtoMappersImpl()
    private val movie1 = with(mapper) { FakeDtoData.Movies.suicideSquad.movieListItemDto.toMovie() }
    private val movie2 = with(mapper) { FakeDtoData.Movies.jungleCruise.movieListItemDto.toMovie() }


    @Test
    fun initViewModel_loadMoviesSuccess_returnMoviesSuccess() = mainCoroutineRule.runBlockingTest {
        Mockito.`when`(moviesRepo.getFavourites()).thenReturn(flow {
            emit(Either.Right(listOf(movie1, movie2)))
        })
        viewModel = FavouritesViewModel(moviesRepo)

        assertThat(viewModel.movies.toList()).isEqualTo(
            listOf(
                Resource.Loading,
                Resource.Success(listOf(movie1, movie2))
            )
        )
    }

    @Test
    fun initViewModel_loadMoviesError_returnMoviesError() = mainCoroutineRule.runBlockingTest {
        val exception = NoSuchElementException()
        Mockito.`when`(moviesRepo.getFavourites()).thenReturn(flow { emit(Either.Left(exception)) })
        viewModel = FavouritesViewModel(moviesRepo)

        assertThat(viewModel.movies.toList()).isEqualTo(
            listOf(
                Resource.Loading,
                Resource.Error(exception)
            )
        )
    }

    @Test
    fun initViewModel_loadMoviesFlow() = mainCoroutineRule.runBlockingTest {
        Mockito.`when`(moviesRepo.getFavourites()).thenReturn(flow {
            emit(Either.Right(listOf(movie1, movie2)))
            emit(Either.Right(listOf(movie1)))
        })
        viewModel = FavouritesViewModel(moviesRepo)

        assertThat(viewModel.movies.toList()).isEqualTo(
            listOf(
                Resource.Loading,
                Resource.Success(listOf(movie1, movie2)),
                Resource.Success(listOf(movie1))
            )
        )
    }


    @Test
    fun onMovieClick() = mainCoroutineRule.runBlockingTest {
        viewModel = FavouritesViewModel(moviesRepo)

        viewModel.onMovieClick(movie1.id)

        assertThat(viewModel.navigateMovieDetails.first()?.getContentIfNotHandled())
            .isEqualTo(movie1.id)
    }

}