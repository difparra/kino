package com.diegoparra.kino.data.network.dtos

import com.google.gson.annotations.SerializedName

data class PersonListItemDto(
    val id: String,
    val name: String?,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("known_for_department") val knownForDepartment: String?,
    @SerializedName("known_for") val knownFor: List<MovieListItemDto>?
)