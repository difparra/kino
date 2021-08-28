package com.diegoparra.kino.data.network.dtos

data class GenresResponse(
    val genres: List<GenreDto>
)

data class GenreDto(
    val id: String,
    val name: String?
)