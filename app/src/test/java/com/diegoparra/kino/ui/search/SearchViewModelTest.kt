package com.diegoparra.kino.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.diegoparra.kino.data.MoviesRepository
import com.diegoparra.kino.data.network.MoviesDtoMappersImpl
import com.diegoparra.kino.test_utils.FakeDtoData
import com.diegoparra.kino.test_utils.MainCoroutineRule
import com.diegoparra.kino.utils.Either
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    //  Rule necessary as I am using liveData in viewModel (when query from savedStateHandle is read)
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var moviesRepo: MoviesRepository
    private lateinit var viewModel: SearchViewModel

    private val mapper = MoviesDtoMappersImpl()
    private val movie1 = with(mapper) { FakeDtoData.Movies.suicideSquad.movieListItemDto.toMovie() }
    private val movie2 = with(mapper) { FakeDtoData.Movies.jungleCruise.movieListItemDto.toMovie() }

    @Before
    fun setUp() {
        val initialQuery = ""
        val savedStateHandle = SavedStateHandle().apply {
            set(SearchViewModel.QUERY_SAVED_STATE_KEY, initialQuery)
        }
        viewModel = SearchViewModel(moviesRepo, savedStateHandle)
    }

    @Test
    fun initialDefaults_emptyQueryGivenInSetUpMethod() = mainCoroutineRule.runBlockingTest {
        assertThat(viewModel.query.first()).isEqualTo("")
        assertThat(viewModel.moviesResult.first()).isEqualTo(SearchResultState.EmptyQuery)
    }

    //  Test throw some exception but passed. The exception is because there is no mocked answer
    //  to the method moviesRepo.searchMovieByName("aveng"), but in this case it is not really
    //  necessary, as I am not testing moviesResult, I am just testing query variable.
    @Test
    fun setQuery() = mainCoroutineRule.runBlockingTest {
        assertThat(viewModel.query.first()).isEqualTo("")

        val query = "aveng"
        viewModel.setQuery(query)
        assertThat(viewModel.query.first()).isEqualTo(query)
    }

    //  Same as in setQuery. An exception is thrown in logcat, but method still passes.
    //  The exception occur because I am not mocking an answer for moviesRepo.searhMovieByName("abc")
    //  but it is not really necessary, as I am just testing query variable, not moviesResult.
    @Test
    fun clearQuery() = mainCoroutineRule.runBlockingTest {
        val initialQuery = "abc"
        viewModel.setQuery(initialQuery)
        assertThat(initialQuery).isNotEmpty()
        assertThat(viewModel.query.first()).isEqualTo(initialQuery)

        viewModel.clearQuery()
        assertThat(viewModel.query.first()).isEqualTo("")
    }

    @Test
    fun moviesResult_happyPath_successStateWithResults() = mainCoroutineRule.runBlockingTest {
        val query = "abc"
        val searchResult = listOf(movie1, movie2)
        Mockito.`when`(moviesRepo.searchMovieByName(query)).thenReturn(Either.Right(searchResult))
        viewModel.setQuery(query)

        assertThat(viewModel.query.first()).isEqualTo(query)
        //  I used this way that is more explicit to compare SearchResultState. Otherwise, I would
        //  have to overwrite equals method on SearchResultState, such as I did for Resource class
        val moviesResult = viewModel.moviesResult.first()
        assertThat(moviesResult).isInstanceOf(SearchResultState.Success::class.java)
        assertThat((moviesResult as SearchResultState.Success).data).isEqualTo(searchResult)
    }

    @Test
    fun moviesResult_emptyQuery() = mainCoroutineRule.runBlockingTest {
        viewModel.clearQuery()
        assertThat(viewModel.query.first()).isEqualTo("")

        assertThat(viewModel.moviesResult.first()).isEqualTo(SearchResultState.EmptyQuery)
    }

    @Test
    fun moviesResult_noSearchResults() = mainCoroutineRule.runBlockingTest {
        val query = "abc"
        Mockito.`when`(moviesRepo.searchMovieByName(query)).thenReturn(Either.Right(emptyList()))
        viewModel.setQuery(query)

        assertThat(viewModel.query.first()).isEqualTo(query)
        assertThat(viewModel.moviesResult.first()).isEqualTo(SearchResultState.NoResults)
    }

    @Test
    fun moviesResult_searchFailure() = mainCoroutineRule.runBlockingTest {
        val query = "abc"
        val exception = NoSuchElementException()
        Mockito.`when`(moviesRepo.searchMovieByName(query)).thenReturn(Either.Left(exception))
        viewModel.setQuery(query)

        assertThat(viewModel.query.first()).isEqualTo(query)
        //  I used this way that is more explicit to compare SearchResultState. Otherwise, I would
        //  have to overwrite equals method on SearchResultState, such as I did for Resource class
        val moviesResult = viewModel.moviesResult.first()
        assertThat(moviesResult).isInstanceOf(SearchResultState.Failure::class.java)
        assertThat((moviesResult as SearchResultState.Failure).exception).isEqualTo(exception)
    }


}