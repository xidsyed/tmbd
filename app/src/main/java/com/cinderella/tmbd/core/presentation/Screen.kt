package com.cinderella.tmbd.core.presentation

import com.cinderella.tmbd.movieList.presentation.MovieListCategory

enum class Screen (val route: String) {
    Home("main"),
    PopularMovieList("popularMovie"),
    UpcomingMovieList("upcomingMovie"),
    Details("details")
}

fun Screen.toMovieCategory() : MovieListCategory{
    return when(this) {
        Screen.PopularMovieList -> MovieListCategory.POPULAR
        Screen.UpcomingMovieList -> MovieListCategory.UPCOMING
        else -> throw (Exception("Invalid screen type"))
    }
}