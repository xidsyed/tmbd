package com.cinderella.tmbd.movieList.presentation

import com.cinderella.tmbd.movieList.domain.model.Movie

data class MovieListState (
    val popularMovieList : List<Movie> = emptyList(),
    val upcomingMovieList : List<Movie> = emptyList(),

    val isLoading : Boolean = true,

    val popularMovieListPage: Int = 1,
    val upcomingMovieListPage: Int = 1,

    val isCurrentPopularScreen: Boolean = true, // TODO : Change to MovieListScreen type
)