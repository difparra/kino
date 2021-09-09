package com.diegoparra.kino.data.network

import com.diegoparra.kino.data.network.dtos.*
import com.diegoparra.kino.models.Genre
import com.diegoparra.kino.models.Movie
import com.diegoparra.kino.models.MovieCredits
import com.diegoparra.kino.models.People

interface MoviesDtoMappers {

    fun toGenreList(genresResponse: GenresResponse): List<Genre>
    fun toMoviesList(moviesListResponse: MoviesListResponse): List<Movie>
    fun toMovie(movieResponse: MovieResponse): Movie
    fun toMovieCredits(creditsResponse: CreditsResponse): MovieCredits
    fun toPeopleList(personListResponse: PersonListResponse): List<People>

    companion object {
        const val NO_INFO_AVAILABLE = "No information available"
    }

}