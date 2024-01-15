package com.cinderella.tmbd.movieList.data.repository

import com.cinderella.tmbd.movieList.data.Mapper.toMovie
import com.cinderella.tmbd.movieList.data.Mapper.toMovieEntity
import com.cinderella.tmbd.movieList.data.local.MovieDatabase
import com.cinderella.tmbd.movieList.data.remote.MovieApi
import com.cinderella.tmbd.movieList.domain.model.Movie
import com.cinderella.tmbd.movieList.domain.repository.MovieListRepository
import com.cinderella.tmbd.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class MovieListRepositoryImpl @Inject constructor(
    private val movieDatabase: MovieDatabase,
    private val movieApi: MovieApi
) : MovieListRepository {
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading())
            val movieDao = movieDatabase.movieDao
            if (forceFetchFromRemote || movieDao.getMovieCount() == 0) {
                try {
                    val remoteList = movieApi.getMovies(category, page)
                    movieDao.upsertMovieList(remoteList.map { it.toMovieEntity(category) })
                } catch (e: Exception) {
                    emit(Resource.Error("Error Getting Movies"))
                    return@flow
                }
            }
            emit(Resource.Success(movieDao.getMovieListByCategory(category).map { it.toMovie() }))
        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading())
            movieDatabase.movieDao.getMovieById(id)?.let { movieEntity ->
                emit(Resource.Success(movieEntity.toMovie()))
            } ?: emit(Resource.Error("Error no such movie"))
        }
    }
}