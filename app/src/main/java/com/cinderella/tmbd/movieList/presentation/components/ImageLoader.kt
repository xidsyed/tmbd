package com.cinderella.tmbd.movieList.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import com.cinderella.tmbd.movieList.domain.model.Movie


@Composable
fun ImageLoader(
    imageState: AsyncImagePainter.State,
    movie: Movie?,
    boxModifier: Modifier,
    imageModifier: Modifier
) {
    if (imageState !is AsyncImagePainter.State.Success) {
        Box(
            modifier = boxModifier, contentAlignment = Alignment.Center
        ) {
            if (imageState is AsyncImagePainter.State.Error) {
                Icon(
                    modifier = Modifier.size(70.dp),
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription = movie?.title,
                )
            }
        }
    } else {
        Image(
            modifier = imageModifier,
            painter = imageState.painter,
            contentDescription = movie?.title,
            contentScale = ContentScale.Crop
        )
    }

}