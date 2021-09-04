package com.diegoparra.kino.ui._utils

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.diegoparra.kino.R

@OptIn(ExperimentalCoilApi::class)
@Composable
fun KinoImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    @DrawableRes placeholder: Int = R.drawable.loading_animation,
    @DrawableRes error: Int = R.drawable.ic_broken_image,
    contentScale: ContentScale = ContentScale.Fit,
    contentDescription: String? = null
) {
    Image(
        painter = rememberImagePainter(
            data = imageUrl,
            builder = {
                size(OriginalSize)
                placeholder(placeholder)
                error(error)
                crossfade(true)
            }
        ),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
    )
}


