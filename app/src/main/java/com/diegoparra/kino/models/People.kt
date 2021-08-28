package com.diegoparra.kino.models

import java.time.LocalDate

data class People(
    val id: String,
    val name: String,
    val birthday: LocalDate?,
    val placeOfBirth: String?,
    val biography: String?
)