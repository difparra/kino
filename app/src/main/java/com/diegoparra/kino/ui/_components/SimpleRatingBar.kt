package com.diegoparra.kino.ui._components

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.diegoparra.kino.ui._utils.FractionalRectangleShape
import com.diegoparra.kino.ui.theme.ColorControl
import com.diegoparra.kino.utils.roundToMultiple

@Composable
fun SimpleRatingBar(
    @IntRange(from = 0, to = 100) ratingPercent: Int,
    numStars: Int = 5,
    width: Dp = (numStars * 20).dp,
    @FloatRange(from = 0.0, to = 1.0) step: Float = 0.0f,
    activeColor: Color = MaterialTheme.colors.primary,
    inactiveColor: Color = ColorControl
) {
    val starSize = (width / numStars) * 0.95f
    val filledStars = (ratingPercent * numStars / 100.0).toFloat()
        .let { if (step > 0.0f && step <= 1.0f) it.roundToMultiple(step) else it }

    Row(
        modifier = Modifier.width(width),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (index in 0 until numStars) {
            if (index != filledStars.toInt()) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    tint = if (index < filledStars) activeColor else inactiveColor,
                    contentDescription = null,
                    modifier = Modifier.size(starSize)
                )
            } else {
                Box {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        tint = inactiveColor,
                        contentDescription = null,
                        modifier = Modifier.size(starSize)
                    )
                    Icon(
                        imageVector = Icons.Filled.Star,
                        tint = activeColor,
                        contentDescription = null,
                        modifier = Modifier
                            .size(starSize)
                            .clip(
                                FractionalRectangleShape(
                                    0f,
                                    (filledStars - filledStars.toInt())
                                )
                            )
                    )
                }
            }
        }
    }

}


@Preview
@Composable
fun SimpleRatingBarPreview() {
    Column {
        SimpleRatingBar(
            width = 200.dp,
            ratingPercent = 50
        )
        SimpleRatingBar(
            width = 100.dp,
            ratingPercent = 50
        )
        SimpleRatingBar(
            numStars = 10,
            ratingPercent = 42
        )
    }
}

@Preview
@Composable
fun FractionalRectangleShapePreview() {
    Column {
        Icon(
            imageVector = Icons.Filled.Star,
            tint = MaterialTheme.colors.primary,
            contentDescription = null,
            modifier = Modifier.clip(FractionalRectangleShape(0f, 0.5f))
        )
        Icon(
            imageVector = Icons.Filled.Star,
            tint = MaterialTheme.colors.primary,
            contentDescription = null,
            modifier = Modifier.clip(FractionalRectangleShape(0f, 0.25f))
        )
    }
}