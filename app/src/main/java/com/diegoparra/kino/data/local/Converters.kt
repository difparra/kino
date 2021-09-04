package com.diegoparra.kino.data.local

import androidx.room.TypeConverter
import java.time.Instant

class Converters {

    @TypeConverter
    fun toInstant(epochMilli: Long): Instant {
        return Instant.ofEpochMilli(epochMilli)
    }

    @TypeConverter
    fun fromInstant(instant: Instant): Long {
        return instant.toEpochMilli()
    }

}