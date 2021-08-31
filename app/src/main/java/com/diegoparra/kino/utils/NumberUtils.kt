package com.diegoparra.kino.utils

fun Float.roundToMultiple(multiple: Float, roundType: RoundType = RoundType.DEFAULT): Float {
    if (this % multiple == 0f) {
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