package com.diegoparra.kino.models

import java.time.LocalDate

data class Movie(
    val id: String,
    val title: String,
    val posterUrl: String,
    val backdropUrl: String = posterUrl,
    val overview: String,
    val genreIds: List<String>,
    val rating: Float?,
    val releaseDate: LocalDate?,
    val runtimeMinutes: Int?
) {
    val year = releaseDate?.year
}