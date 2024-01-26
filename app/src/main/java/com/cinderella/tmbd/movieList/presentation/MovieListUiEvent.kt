package com.cinderella.tmbd.movieList.presentation

sealed interface MovieListUiEvent {
    data class Paginate(val category: MovieListCategory) : MovieListUiEvent
    object Navigate : MovieListUiEvent
}

enum class MovieListCategory(val value: String) {
    POPULAR("popular"),
    UPCOMING("upcoming")
}