package com.diegoparra.kino.data.local

import androidx.room.TypeConverter
import com.diegoparra.kino.utils.toEpochMilliUTC
import com.diegoparra.kino.utils.toLocalDateTime
import java.time.LocalDateTime

class Converters {

    @TypeConverter
    fun toLocalDateTime(epochSecond: Long): LocalDateTime {
        return epochSecond.toLocalDateTime()
    }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime): Long {
        return dateTime.toEpochMilliUTC()
    }

}