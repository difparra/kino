package com.diegoparra.kino.ui.utils

import androidx.annotation.FloatRange
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class FractionalRectangleShape(
    @FloatRange(from = 0.0, to = 1.0) private val startFraction: Float,
    @FloatRange(from = 0.0, to = 1.0) private val endFraction: Float
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            Rect(
                left = (startFraction * size.width).coerceAtMost(size.width - 1f),
                top = 0f,
                right = (endFraction * size.width).coerceAtLeast(1f),
                bottom = size.height
            )
        )
    }

}