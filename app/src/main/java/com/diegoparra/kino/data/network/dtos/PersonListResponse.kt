package com.diegoparra.kino.data.network.dtos

import com.google.gson.annotations.SerializedName

data class PersonListResponse(
    val page: Int,
    val results: List<PersonListItemDto>,
    @SerializedName("total_pages") val totalPages: Int?,
    @SerializedName("total_results") val totalResults: Int?
)