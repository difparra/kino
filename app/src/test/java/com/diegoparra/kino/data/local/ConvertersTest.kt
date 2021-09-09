package com.diegoparra.kino.data.local

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant

class ConvertersTest {

    //  EpochMillis UTC of Tuesday, 7. September 2021 20:00:00
    private val epochMillis = 1631044800000L


    @Test
    fun toInstant_fromEpochMillis_returnInstant() {
        val calculatedInstant = Converters().toInstant(epochMillis)
        assertThat(calculatedInstant.toEpochMilli()).isEqualTo(epochMillis)
    }

    @Test
    fun fromInstant_givenInstant_returnEpochMillisUTC() {
        val instant = Instant.ofEpochMilli(epochMillis)
        val calculatedEpochMillis = Converters().fromInstant(instant)
        assertThat(calculatedEpochMillis).isEqualTo(epochMillis)
    }

}