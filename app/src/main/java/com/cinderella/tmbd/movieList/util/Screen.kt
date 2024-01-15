package com.cinderella.tmbd.movieList.util

import androidx.compose.ui.input.key.Key.Companion.Home

enum class Screen (val route: String) {
    Home("main"),
    PopularMovieList("popularMovie"),
    UpcomingMovieList("upcomingMovie"),
    Details("details")
}