package com.cinderella.tmbd.core.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cinderella.tmbd.R
import com.cinderella.tmbd.movieList.presentation.MovieListUiEvent
import com.cinderella.tmbd.movieList.presentation.MovieListViewModel
import com.cinderella.tmbd.movieList.presentation.screens.movielist.MovieListScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen (navController: NavHostController) {
    val viewModel = hiltViewModel<MovieListViewModel>()
    val state = viewModel.movieListState.collectAsStateWithLifecycle().value
    val bottomNavController = rememberNavController()
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = if (state.isCurrentPopularScreen) stringResource(R.string.popular_movies)
                    else stringResource(R.string.upcoming_movies), fontSize = 20.sp
                )
            },
            modifier = Modifier.shadow(2.dp),
            colors = TopAppBarDefaults.smallTopAppBarColors(
                MaterialTheme.colorScheme.inverseOnSurface
            )
        )
    }, bottomBar = {
        BottomNavigationBar(
            bottomNavController = bottomNavController,
            onEvent = viewModel::onUiEvent
        )
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = Screen.PopularMovieList.route    // TODO : derive from viewmodel.state.currentScreen
            ) {
                composable(Screen.PopularMovieList.route) {
                    Log.d("TAG", "HomeScreen: Popular Route")
                    MovieListScreen(
                        state = state,
                        screen = Screen.PopularMovieList,
                        navController = navController,
                        onUiEvent = viewModel::onUiEvent
                    )
                }
                composable(Screen.UpcomingMovieList.route) {
                    Log.d("TAG", "HomeScreen: Upcoming Route")
                    MovieListScreen(
                        state = state,
                        screen = Screen.UpcomingMovieList,
                        navController = navController,
                        onUiEvent = viewModel::onUiEvent
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    bottomNavController: NavHostController, onEvent: (MovieListUiEvent.Navigate) -> Unit
) {
    val items = listOf(
        BottomNavItem(stringResource(R.string.popular), Icons.Rounded.Movie),
        BottomNavItem(stringResource(R.string.upcoming), Icons.Rounded.Upcoming),
    )

    var selectedIndex by rememberSaveable { mutableStateOf(0) }

    NavigationBar {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEachIndexed { index, bottomNavItem ->
                Add(bottomNavItem, index, selectedIndex) {
                    selectedIndex = index
                    onEvent(MovieListUiEvent.Navigate)
                    when (index) {
                        0 -> {
                            bottomNavController.popBackStack()
                            bottomNavController.navigate(Screen.PopularMovieList.route)
                        }

                        1 -> {
                            bottomNavController.popBackStack()
                            bottomNavController.navigate(Screen.UpcomingMovieList.route)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.Add(
    navItem: BottomNavItem,
    index: Int,
    selectedIndex: Int,
    onClick : () -> Unit
) {
    NavigationBarItem(icon = {
        Icon(
            imageVector = navItem.icon,
            contentDescription = navItem.title
        )
    },
        label = { Text(navItem.title) },
        selected = index == selectedIndex,
        onClick = { onClick() }
    )

}

data class BottomNavItem(
    val title: String, val icon: ImageVector
)
