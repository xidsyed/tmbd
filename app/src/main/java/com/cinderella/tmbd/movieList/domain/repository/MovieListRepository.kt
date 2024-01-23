package com.cinderella.tmbd.movieList.domain.repository

import com.cinderella.tmbd.movieList.domain.model.Movie
import com.cinderella.tmbd.movieList.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {
    suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int? = 1
    ): Flow<Resource<List<Movie>>>

    suspend fun getMovie(id: Int): Flow<Resource<Movie>>
}