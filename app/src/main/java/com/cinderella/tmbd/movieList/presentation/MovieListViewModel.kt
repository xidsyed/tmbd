package com.cinderella.tmbd.movieList.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinderella.tmbd.movieList.domain.repository.MovieListRepository
import com.cinderella.tmbd.movieList.util.Category.POPULAR
import com.cinderella.tmbd.movieList.util.Category.UPCOMING
import com.cinderella.tmbd.movieList.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository
) : ViewModel() {
    private val _movieListState = MutableStateFlow(MovieListState())
    val movieListState: StateFlow<MovieListState>
        get() = _movieListState.asStateFlow()

    private val _viewModelEventsStateChannel = Channel<ViewModelEvent>()
    val viewModelEventsStateFlow: Flow<ViewModelEvent>
        get() = _viewModelEventsStateChannel.receiveAsFlow()

    init {
        getPopularMoviesList(true)
        getPopularMoviesList(true)
    }

    fun onUiEvent(event: MovieListUiEvent) {
        when (event) {
            is MovieListUiEvent.Paginate -> {
                if (event.category == POPULAR) {
                    getPopularMoviesList(true)

                } else if (event.category == UPCOMING) {
                    getUpcomingMovieList(true)
                }
            }

            is MovieListUiEvent.Navigate -> {
                _movieListState.update {
                    it.copy(isCurrentPopularScreen = !it.isCurrentPopularScreen)
                }
            }
        }
    }

    fun getPopularMoviesList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            movieListRepository.getMovieList(
                forceFetchFromRemote,
                POPULAR,
                movieListState.value.popularMovieListPage
            ).collectLatest { resource ->
                val data = resource.data
                when (resource) {
                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = true)
                        }
                        Log.d("TAG", "getPopularMoviesList: LOADING")

                    }

                    is Resource.Success -> {
                        _movieListState.update {
                            it.copy(
                                isLoading = false,
                                popularMovieListPage = it.popularMovieListPage + 1,
                                popularMovieList = data!!
                            )
                        }
                        Log.d("TAG", "getPopularMoviesList: SUCCESS \n ${data}")
                    }


                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                        Log.d("TAG", "getPopularMoviesList: ERROR ${resource.message}")

                        _viewModelEventsStateChannel.send(
                            ViewModelEvent.ErrorEvent(
                                resource.message ?: "Something Went Wrong"
                            )
                        )
                    }
                }
            }
        }
    }

    fun getUpcomingMovieList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            movieListRepository.getMovieList(
                forceFetchFromRemote,
                UPCOMING,
                movieListState.value.upcomingMovieListPage
            ).collectLatest { resource ->
                val data = resource.data
                when (resource) {
                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _movieListState.update {
                            it.copy(
                                isLoading = false,
                                upcomingMovieListPage = it.upcomingMovieListPage + 1,
                                upcomingMovieList = data!!
                            )
                        }
                    }

                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }
                }
            }
        }
    }

}

sealed class ViewModelEvent() {
    class ErrorEvent(message: String) : ViewModelEvent()

}

