package com.diegoparra.kino.data.network.dtos

import com.google.gson.annotations.SerializedName

data class CreditsResponse(
    @SerializedName("id") val movieId: String,
    val cast: List<CastDto>,
    val crew: List<CrewDto>
)

data class CastDto(
    val id: String,
    @SerializedName("known_for_department") val knownForDepartment: String?,
    val name: String?,
    @SerializedName("profile_path") val profilePath: String?,
    val character: String?
)

data class CrewDto(
    val id: String,
    @SerializedName("known_for_department") val knownForDepartment: String?,
    val name: String?,
    @SerializedName("profile_path") val profilePath: String?,
    val job: String?
)