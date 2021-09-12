package com.diegoparra.kino.ui.home

import com.diegoparra.kino.data.MoviesRepository
import com.diegoparra.kino.data.network.MoviesDtoMappersImpl
import com.diegoparra.kino.models.Genre
import com.diegoparra.kino.models.GenreWithMovies
import com.diegoparra.kino.test_utils.FakeDtoData
import com.diegoparra.kino.test_utils.MainCoroutineRule
import com.diegoparra.kino.utils.Either
import com.diegoparra.kino.utils.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var moviesRepository: MoviesRepository
    private lateinit var homeViewModel: HomeViewModel

    private val mapper = MoviesDtoMappersImpl()
    private val genre1 = with(mapper) { FakeDtoData.Genres.action.toGenre() }
    private val genre2 = with(mapper) { FakeDtoData.Genres.comedy.toGenre() }
    private val movie1 = with(mapper) { FakeDtoData.Movies.suicideSquad.movieListItemDto.toMovie() }
    private val movie2 = with(mapper) { FakeDtoData.Movies.jungleCruise.movieListItemDto.toMovie() }


    /*
        Hadn't defined a setUp method as I am always testing viewModel init/constructor method
        when conditions are not the same.
     */

    @Test
    fun initViewModel_loadGenresSuccess_returnGenresSuccess() = mainCoroutineRule.runBlockingTest {
        val genresList = listOf(genre1, genre2)
        Mockito.`when`(moviesRepository.getGenres()).thenReturn(Either.Right(genresList))
        homeViewModel = HomeViewModel(moviesRepository)

        assertThat(homeViewModel.failure.first()).isNull()
        assertThat(homeViewModel.genres.first()).isEqualTo(genresList)
        assertThat(homeViewModel.loading.first()).isEqualTo(false)
    }

    @Test
    fun initViewModel_loadGenresError_returnFailureWithGenresError() =
        mainCoroutineRule.runBlockingTest {
            val exception = NoSuchElementException()
            Mockito.`when`(moviesRepository.getGenres()).thenReturn(Either.Left(exception))
            homeViewModel = HomeViewModel(moviesRepository)

            assertThat(homeViewModel.failure.first()?.getContentIfNotHandled()).isEqualTo(exception)
            assertThat(homeViewModel.genres.first()).isEqualTo(emptyList<Genre>())
            assertThat(homeViewModel.loading.first()).isEqualTo(false)
        }

    @Test
    fun genreAndMovies_happyPath() = mainCoroutineRule.runBlockingTest {
        Mockito.`when`(moviesRepository.getGenres())
            .thenReturn(Either.Right(listOf(genre1, genre2)))
        Mockito.`when`(moviesRepository.getMoviesByGenre(genre1.id))
            .thenReturn(Either.Right(listOf(movie1)))
        Mockito.`when`(moviesRepository.getMoviesByGenre(genre2.id))
            .thenReturn(Either.Right(listOf(movie2)))
        homeViewModel = HomeViewModel(moviesRepository)

        assertThat(homeViewModel.failure.first()).isNull()
        assertThat(homeViewModel.genres.first()).isEqualTo(listOf(genre1, genre2))
        assertThat(homeViewModel.loading.first()).isEqualTo(false)
        assertThat(homeViewModel.genreAndMovies.first()).isEqualTo(
            listOf<Resource<GenreWithMovies>>(
                Resource.Success(GenreWithMovies(genre1, listOf(movie1))),
                Resource.Success(GenreWithMovies(genre2, listOf(movie2)))
            )
        )
    }

    @Test
    fun genreAndMovies_loadGenresError_returnGenreAndMoviesIsEmpty() =
        mainCoroutineRule.runBlockingTest {
            val exception = NoSuchElementException()
            Mockito.`when`(moviesRepository.getGenres()).thenReturn(Either.Left(exception))
            homeViewModel = HomeViewModel(moviesRepository)

            assertThat(homeViewModel.failure.first()?.getContentIfNotHandled()).isEqualTo(exception)
            assertThat(homeViewModel.genres.first()).isEmpty()
            assertThat(homeViewModel.loading.first()).isEqualTo(false)
            assertThat(homeViewModel.genreAndMovies.first()).isEmpty()
        }

    @Test
    fun genreAndMovies_loadMoviesByGenreContainsAnError_returnListWithSuccessAndError() =
        mainCoroutineRule.runBlockingTest {
            val exception = NoSuchElementException()
            Mockito.`when`(moviesRepository.getGenres())
                .thenReturn(Either.Right(listOf(genre1, genre2)))
            Mockito.`when`(moviesRepository.getMoviesByGenre(genre1.id))
                .thenReturn(Either.Right(listOf(movie1, movie2)))
            Mockito.`when`(moviesRepository.getMoviesByGenre(genre2.id))
                .thenReturn(Either.Left(exception))
            homeViewModel = HomeViewModel(moviesRepository)

            assertThat(homeViewModel.failure.first()).isNull()
            assertThat(homeViewModel.genres.first()).isEqualTo(listOf(genre1, genre2))
            assertThat(homeViewModel.loading.first()).isEqualTo(false)
            assertThat(homeViewModel.genreAndMovies.first()).isEqualTo(
                listOf(
                    Resource.Success(GenreWithMovies(genre1, listOf(movie1, movie2))),
                    Resource.Error(exception)
                )
            )
        }

    @Test
    fun onMovieClick_navigateChangeItsValue() = mainCoroutineRule.runBlockingTest {
        homeViewModel = HomeViewModel(moviesRepository)

        homeViewModel.onMovieClick("123")

        assertThat(homeViewModel.navigateMovieDetails.first()?.getContentIfNotHandled())
            .isEqualTo("123")
    }

}