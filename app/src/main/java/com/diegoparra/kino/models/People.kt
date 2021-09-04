package com.diegoparra.kino.models

data class People(
    val id: String,
    val name: String,
    val profilePicUrl: String,
    val department: Department,
    val knownFor: List<Movie>?
) {
    enum class Department { ACTING, PRODUCTION, UNKNOWN }
}