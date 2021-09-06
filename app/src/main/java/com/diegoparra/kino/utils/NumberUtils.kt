package com.diegoparra.kino.utils

private const val THRESHOLD = 0.001f            //  To compare float numbers, as they can be not exact in every operation

/**
 * Round a float positive number to a multiple of some other number
 * E.g. Round 2.4 to a multiple of 0.5 will return 2.0 if RoundType.Floor or 2.5 in other case
 */
fun Float.roundToMultiple(multiple: Float, roundType: RoundType = RoundType.DEFAULT): Float {
    if (multiple < THRESHOLD || this < 0.0f) {
        throw IllegalArgumentException()
    }
    if (this == 0.0f || this % multiple < THRESHOLD) {
        return this
    }
    return when (roundType) {
        RoundType.CEIL -> ((this / multiple).toInt() * multiple) + multiple
        RoundType.FLOOR -> (this / multiple).toInt() * multiple
        RoundType.DEFAULT -> {
            if (this % multiple >= multiple / 2) {
                ((this / multiple).toInt() * multiple) + multiple
            } else {
                (this / multiple).toInt() * multiple
            }
        }
    }
}

enum class RoundType { DEFAULT, CEIL, FLOOR }