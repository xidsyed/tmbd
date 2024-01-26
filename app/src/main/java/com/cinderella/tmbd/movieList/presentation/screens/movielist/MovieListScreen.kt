package com.cinderella.tmbd.movieList.presentation.screens.movielist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cinderella.tmbd.core.presentation.Screen
import com.cinderella.tmbd.core.presentation.toMovieCategory
import com.cinderella.tmbd.movieList.domain.model.Movie
import com.cinderella.tmbd.movieList.presentation.MovieListState
import com.cinderella.tmbd.movieList.presentation.MovieListUiEvent
import com.cinderella.tmbd.movieList.presentation.screens.movielist.components.MovieListItem

@Composable
fun MovieListScreen(
    state: MovieListState,
    screen: Screen,
    navController: NavHostController,
    onUiEvent: (MovieListUiEvent) -> Unit
) {
    val movies = MovieList(
        screen = screen, list = if (screen == Screen.PopularMovieList) state.popularMovieList
        else state.upcomingMovieList
    )

    if (movies.list.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
    } else {
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(movies.list.size) { index ->
                MovieListItem(movie = movies.list[index], navController = navController)
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )
                if (index >= movies.list.size - 1) {
                    onUiEvent(MovieListUiEvent.Paginate(movies.screen.toMovieCategory()))
                }
            }

        }
    }
}

private data class MovieList(val screen: Screen, val list: List<Movie>)

