package com.diegoparra.kino.utils

import com.diegoparra.kino.test_utils.assertThrows
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class NumberUtilsTest {

    private val multiple: Float = 1.7f
    private val timesMultipleToNumToRound: Int = 3
        //  Number of times multiple will be multiplied to get a base number to round, and have an
        //  idea of what the result will be.


    /*
        --------------------------------------------------------------------------------------------
            ILLEGAL ARGUMENTS
        --------------------------------------------------------------------------------------------
    */

    @Test
    fun roundToMultiple_multiple0_throwIllegalArgumentException() {
        assertThrows<IllegalArgumentException> {
            1f.roundToMultiple(multiple = 0f)
        }
    }

    @Test
    fun roundToMultiple_multipleLowerThan0_throwIllegalArgumentException() {
        var isException = false
        try {
            1f.roundToMultiple(multiple = -1f)
        } catch (e: Exception) {
            isException = true
            assertThat(e).isInstanceOf(IllegalArgumentException::class.java)
        } finally {
            assertThat(isException).isTrue()
        }
    }

    @Test
    fun roundToMultiple_negativeNumber_throwIllegalArgumentException() {
        var isException = false
        try {
            (-1f).roundToMultiple(multiple = 1f)
        } catch (e: Exception) {
            isException = true
            assertThat(e).isInstanceOf(IllegalArgumentException::class.java)
        } finally {
            assertThat(isException).isTrue()
        }
    }

    @Test
    fun roundToMultiple_round0_return0() {
        assertThat(0.0f.roundToMultiple(1f)).isEqualTo(0)
    }

    /*
        --------------------------------------------------------------------------------------------
            ROUND TYPES TESTING
        --------------------------------------------------------------------------------------------
    */

    @Test
    fun roundToMultiple_exact_returnOriginalNumberIndependentOnRoundType() {
        val numToRound = multiple * timesMultipleToNumToRound

        assertThat(numToRound.roundToMultiple(multiple, RoundType.DEFAULT)).isEqualTo(numToRound)
        assertThat(numToRound.roundToMultiple(multiple, RoundType.CEIL)).isEqualTo(numToRound)
        assertThat(numToRound.roundToMultiple(multiple, RoundType.FLOOR)).isEqualTo(numToRound)
    }

    @Test
    fun roundToMultiple_half_returnFloorWhenFloorAndCeilWhenCeilOrDefault() {
        val numToRound = (multiple * timesMultipleToNumToRound) + (multiple / 2)
        val roundedCeil = multiple * (timesMultipleToNumToRound + 1)
        val roundedFloor = multiple * timesMultipleToNumToRound
        assertThat(numToRound).isEqualTo((multiple * timesMultipleToNumToRound) + (multiple / 2))
        assertThat(roundedCeil).isAtLeast(numToRound)
        assertThat(roundedFloor).isAtMost(numToRound)

        assertThat(numToRound.roundToMultiple(multiple, RoundType.DEFAULT)).isEqualTo(roundedCeil)
        assertThat(numToRound.roundToMultiple(multiple, RoundType.CEIL)).isEqualTo(roundedCeil)
        assertThat(numToRound.roundToMultiple(multiple, RoundType.FLOOR)).isEqualTo(roundedFloor)
    }

    @Test
    fun roundToMultiple_lowerThanHalf_returnFloorWhenFloorOrDefaultAndCeilWhenCeil() {
        val numToRound = (multiple * timesMultipleToNumToRound) + (multiple / 2) - (multiple / 4)
        val roundedCeil = multiple * (timesMultipleToNumToRound + 1)
        val roundedFloor = multiple * timesMultipleToNumToRound
        assertThat(numToRound).isLessThan((multiple * timesMultipleToNumToRound) + (multiple / 2))
        assertThat(roundedCeil).isAtLeast(numToRound)
        assertThat(roundedFloor).isAtMost(numToRound)

        assertThat(numToRound.roundToMultiple(multiple, RoundType.DEFAULT)).isEqualTo(roundedFloor)
        assertThat(numToRound.roundToMultiple(multiple, RoundType.CEIL)).isEqualTo(roundedCeil)
        assertThat(numToRound.roundToMultiple(multiple, RoundType.FLOOR)).isEqualTo(roundedFloor)
    }

    @Test
    fun roundToMultiple_greaterThanHalf_returnFloorWhenFloorAndCeilWhenCeilOrDefault() {
        val numToRound = (multiple * timesMultipleToNumToRound) + (multiple / 2) + (multiple / 4)
        val roundedCeil = multiple * (timesMultipleToNumToRound + 1)
        val roundedFloor = multiple * timesMultipleToNumToRound
        assertThat(numToRound).isGreaterThan((multiple * timesMultipleToNumToRound) + (multiple / 2))
        assertThat(roundedCeil).isAtLeast(numToRound)
        assertThat(roundedFloor).isAtMost(numToRound)

        assertThat(numToRound.roundToMultiple(multiple, RoundType.DEFAULT)).isEqualTo(roundedCeil)
        assertThat(numToRound.roundToMultiple(multiple, RoundType.CEIL)).isEqualTo(roundedCeil)
        assertThat(numToRound.roundToMultiple(multiple, RoundType.FLOOR)).isEqualTo(roundedFloor)
    }

}