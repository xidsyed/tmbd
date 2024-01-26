package com.cinderella.tmbd.movieList.presentation.screens.movielist.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.cinderella.tmbd.movieList.data.remote.MovieApi
import com.cinderella.tmbd.movieList.domain.model.Movie
import com.cinderella.tmbd.movieList.presentation.components.ImageLoader
import com.cinderella.tmbd.movieList.util.RatingBar
import com.cinderella.tmbd.movieList.util.addEmptyLines
import com.cinderella.tmbd.movieList.util.getAverageColor


@Composable
fun MovieListItem(movie: Movie, navController: NavHostController) {
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL + movie.poster_path).size(Size.ORIGINAL).build()

    ).state

    val defaultColor = MaterialTheme.colorScheme.secondaryContainer
    var dominantColor by remember { mutableStateOf(defaultColor) }
    LaunchedEffect(imageState) {
        if (imageState is AsyncImagePainter.State.Success) {
            dominantColor = getAverageColor(imageState.result.drawable.toBitmap().asImageBitmap())
        }
    }

    Column(modifier = Modifier
        .wrapContentHeight()
        .width(200.dp)
        .padding(8.dp)
        .clip(RoundedCornerShape(ROUNDED_CORNER_RADIUS))
        .background(Brush.verticalGradient(listOf(defaultColor, dominantColor)))
        .clickable {
            Log.d("TAG", "MovieListItem: navController Clicked!!!")
            navController.navigate("details/${movie.id}")
            Log.d("TAG", "MovieListItem: Click Registered!!!")
        }) {
        ImageLoader(
            imageState,
            movie,
            boxModifier = Modifier
                .padding(6.dp)
                .height(IMAGE_HEIGHT)
                .clip(RoundedCornerShape(ROUNDED_CORNER_RADIUS))
                .fillMaxWidth()
                .background(Color.Transparent),
            imageModifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .height(IMAGE_HEIGHT)
                .clip(RoundedCornerShape(ROUNDED_CORNER_RADIUS))
        )
        Spacer(modifier = Modifier.height(6.dp))

        var updatedText by remember { mutableStateOf(movie.title) }
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp),
            text = updatedText,
            color = Color.White,
            maxLines = TITLE_LINE_COUNT,
            onTextLayout = { textLayoutResult ->
                if (textLayoutResult.lineCount < TITLE_LINE_COUNT) {
                    updatedText =
                        updatedText.addEmptyLines(TITLE_LINE_COUNT - textLayoutResult.lineCount)
                }
            },
            overflow = TextOverflow.Ellipsis,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, bottom = 12.dp, top = 4.dp)
        ) {
            RatingBar(
                starsModifier = Modifier.size(18.dp),
                rating = movie.vote_average / 2,
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = movie.vote_average.toString().take(3),
                color = Color.LightGray,
                fontSize = 14.sp,
                maxLines = 1
            )
        }
    }

}

private val IMAGE_HEIGHT = 250.dp
private val ROUNDED_CORNER_RADIUS = 22.dp
private val TITLE_LINE_COUNT = 2
