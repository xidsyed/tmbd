package com.cinderella.tmbd

import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.cinderella.tmbd.movieList.presentation.MovieListState
import com.cinderella.tmbd.movieList.presentation.MovieListUiEvent
import com.cinderella.tmbd.movieList.presentation.MovieListViewModel
import com.cinderella.tmbd.ui.theme.TMBDTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TMBDTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = hiltViewModel<MovieListViewModel>()
                    val lifecycleOwner = LocalLifecycleOwner.current
                    var state = viewModel.movieListState.collectAsStateWithLifecycle()
                    MovieListScreen(state = state, onUiEvent = viewModel::onUiEvent)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume();
        if (BuildConfig.DEBUG) { // don't even consider it otherwise
                Log.d("SCREEN", "Keeping screen on for debugging, detach debugger and force an onResume to turn it off.");
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
}

@Composable
fun MovieListScreen(state: State<MovieListState>, onUiEvent: (MovieListUiEvent) -> Unit) {
    Log.d("TAG", "MovieListScreen: ${state.value.popularMovieList}")
    when(state.value) {

    }
}

