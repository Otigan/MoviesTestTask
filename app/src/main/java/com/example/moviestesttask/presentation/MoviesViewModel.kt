package com.example.moviestesttask.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviestesttask.data.util.Resource
import com.example.moviestesttask.domain.entity.Film
import com.example.moviestesttask.domain.entity.ListItem
import com.example.moviestesttask.domain.use_case.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MovieEvent {
    data class Success(val movies: List<ListItem>) : MovieEvent()
    object Loading : MovieEvent()
    data class Error(val errorMessage: String) : MovieEvent()
    object Empty : MovieEvent()
}

@HiltViewModel
class MoviesViewModel @Inject constructor(private val getMoviesUseCase: GetMoviesUseCase) :
    ViewModel() {

    private val _movies = MutableStateFlow<MovieEvent>(MovieEvent.Empty)
    val movies = _movies.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getMoviesUseCase().collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> {
                        resource.errorMessage?.let {
                            _movies.value = MovieEvent.Error(it)
                        }
                    }
                    is Resource.Loading -> {
                        _movies.value = MovieEvent.Loading
                    }
                    is Resource.Success -> {
                        resource.data?.let {
                            val finalList = mergeLists(it)
                            _movies.value = MovieEvent.Success(finalList)
                        }
                    }
                }
            }
        }
    }

    private fun mergeLists(movies: List<Film>): List<ListItem> {
        val genres = mutableListOf<String>()
        val sortedMovies = movies.sortedBy { it.localized_name }
        val finalList = mutableListOf<ListItem>()
        for (movie in sortedMovies) {
            val listItemMovie = ListItem.Film(
                movie.id,
                movie.name,
                movie.localized_name,
                movie.year,
                movie.rating,
                movie.image_url,
                movie.description,
                movie.genres
            )
            finalList.add(listItemMovie)
            genres.addAll(movie.genres)
        }
        val distinctGenres = genres.distinct().map {
            ListItem.Genre(it)
        }
        finalList.addAll(0, distinctGenres)
        return finalList.toList()
    }

}