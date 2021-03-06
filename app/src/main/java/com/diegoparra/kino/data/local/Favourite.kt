package com.diegoparra.kino.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "Favourite")
data class Favourite(
    @PrimaryKey val movieId: String,
    val updatedAt: Instant
)