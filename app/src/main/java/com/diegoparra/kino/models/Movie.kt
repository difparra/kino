package com.diegoparra.kino.models

import androidx.annotation.IntRange
import java.time.LocalDate

data class Movie(
    val id: String,
    val title: String,
    val posterUrl: String,
    val backdropUrl: String = posterUrl,
    val overview: String,
    val genreIds: List<String>,
    @IntRange(from = 0, to = 100) val rating: Int?,
    val releaseDate: LocalDate?,
    val runtimeMinutes: Int?
) {
    val year = releaseDate?.year
}