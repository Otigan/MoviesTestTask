package com.example.moviestesttask.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviestesttask.data.util.Resource
import com.example.moviestesttask.domain.use_case.FilterMoviesUseCase
import com.example.moviestesttask.domain.use_case.GetGenresUseCase
import com.example.moviestesttask.domain.use_case.GetMoviesUseCase
import com.example.moviestesttask.presentation.util.FilmEvent
import com.example.moviestesttask.presentation.util.GenreEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class MoviesViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val filterMoviesUseCase: FilterMoviesUseCase,
    private val getGenresUseCase: GetGenresUseCase
) : ViewModel() {

    private var currentQuery: String = ""

    private val _films = MutableStateFlow<FilmEvent>(FilmEvent.Empty)
    val films = _films.asStateFlow()

    private val _genres = MutableStateFlow<GenreEvent>(GenreEvent.Empty)
    val genres = _genres.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                getMoviesUseCase().collectLatest { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            resource.errorMessage?.let {
                                _films.value = FilmEvent.Error(it)
                            }
                        }
                        is Resource.Loading -> {
                            _films.value = FilmEvent.Loading
                        }
                        is Resource.Success -> {
                            resource.data?.let { films ->
                                _films.value = FilmEvent.Success(films)
                            }
                        }
                    }
                }
            }
            launch {
                getGenresUseCase().collectLatest { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            resource.errorMessage?.let {
                                _genres.value = GenreEvent.Error(it)
                            }
                        }
                        is Resource.Loading -> {
                            _genres.value = GenreEvent.Loading
                        }
                        is Resource.Success -> {
                            resource.data?.let { genres ->
                                _genres.value = GenreEvent.Success(genres)
                            }
                        }
                    }
                }
            }
        }
    }

    fun filterMovies(query: String) {
        viewModelScope.launch {
            currentQuery = if (currentQuery == query) {
                ""
            } else {
                query
            }
            filterMoviesUseCase(currentQuery).collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> {
                        resource.errorMessage?.let {
                            _films.value = FilmEvent.Error(it)
                        }
                    }
                    is Resource.Loading -> {
                        _films.value = FilmEvent.Loading
                    }
                    is Resource.Success -> {
                        resource.data?.let { genres ->
                            _films.value = FilmEvent.Success(genres)
                        }
                    }
                }
            }
        }
    }
}