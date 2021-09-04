package com.diegoparra.kino.data.network

import com.diegoparra.kino.data.network.dtos.*
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
    suspend fun getMoviesByGenre(@Query("with_genres") genreId: String): MoviesListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieById(@Path("movie_id") movieId: String): MovieDto

    @GET("movie/{movie_id}/credits")
    suspend fun getCredits(@Path("movie_id") movieId: String): CreditsResponse

    @GET("movie/{movie_id}/recommendations")
    suspend fun getSuggestions(@Path("movie_id") movieId: String): MoviesListResponse

    @GET("search/movie")
    suspend fun searchMovieByName(@Query("query") name: String): MoviesListResponse

    @GET("search/person")
    suspend fun searchPeopleByName(@Query("query") name: String): PersonListResponse

    @GET("discover/movie")
    suspend fun searchMovieByActorId(@Query("with_people") actorId: String): MoviesListResponse


    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_URL_PREFIX = "https://image.tmdb.org/t/p/original"
    }

}