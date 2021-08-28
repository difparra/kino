package com.diegoparra.kino.data.network

import com.diegoparra.kino.data.network.dtos.GenresResponse
import com.diegoparra.kino.data.network.dtos.MovieDto
import com.diegoparra.kino.data.network.dtos.MoviesByGenreResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * RestAPI documentation - The Movie Database documentation:
 * https://developers.themoviedb.org/3/movies/get-movie-details
 */
interface MoviesApi {

    @GET("genre/movie/list")
    suspend fun getGenres(): GenresResponse

    @GET("discover/movie")
    suspend fun getMoviesByGenre(@Query("with_genres") genreId: String): MoviesByGenreResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieById(@Path("movie_id") movieId: String): MovieDto


    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }

}