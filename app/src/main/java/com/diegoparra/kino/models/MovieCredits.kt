package com.diegoparra.kino.models

data class MovieCredits(
    val movieId: String,
    val characters: List<Character>,
    val producer: People
)