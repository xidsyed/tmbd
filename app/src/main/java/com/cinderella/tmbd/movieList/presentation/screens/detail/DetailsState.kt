package com.cinderella.tmbd.movieList.presentation.screens.detail

import com.cinderella.tmbd.movieList.domain.model.Movie

data class DetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)