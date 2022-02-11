package com.example.moviestesttask.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviestesttask.data.util.Resource
import com.example.moviestesttask.domain.entity.Film
import com.example.moviestesttask.domain.entity.GenreListItem
import com.example.moviestesttask.domain.entity.MovieListItem
import com.example.moviestesttask.domain.use_case.FilterMoviesUseCase
import com.example.moviestesttask.domain.use_case.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MovieEvent {
    data class Success(val movies: List<MovieListItem>, val genres: List<GenreListItem>) :
        MovieEvent()

    object Loading : MovieEvent()
    data class Error(val errorMessage: String) : MovieEvent()
    object Empty : MovieEvent()
}

@HiltViewModel
@ExperimentalCoroutinesApi
class MoviesViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val filterMoviesUseCase: FilterMoviesUseCase
) :
    ViewModel() {

    private val _filterQuery = MutableStateFlow("")
    val filteredMovies = _filterQuery.flatMapLatest {
        filterMoviesUseCase(it)
    }

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
                        resource.data?.let { films ->
                            val genres = getGenresList(films)
                            Log.d("ViewModel", ": ${genres[0]}")
                            val filmItems = films.map {
                                MovieListItem.Film(
                                    it.id,
                                    it.localized_name,
                                    it.name,
                                    it.year,
                                    it.rating,
                                    it.image_url,
                                    it.description,
                                    it.genres
                                )
                            }
                            val listItems =
                                mutableListOf<MovieListItem>(MovieListItem.Header("Фильмы"))
                            listItems.addAll(filmItems)
                            _movies.value = MovieEvent.Success(listItems, genres)
                        }
                    }
                }
            }
        }
    }

    fun filterMovies(query: String) {
        if (query == _filterQuery.value) {
            _filterQuery.value = ""
        } else {
            _filterQuery.value = query
        }
    }

    private fun getGenresList(movies: List<Film>): List<GenreListItem> {
        val genres = mutableListOf<String>()
        for (movie in movies) {
            genres.addAll(movie.genres)
        }
        val distinctGenres = genres.distinct().map {
            GenreListItem.Genre(it)
        }
        val listItems =
            mutableListOf<GenreListItem>(GenreListItem.Header("Жанры"))
        listItems.addAll(distinctGenres)
        return listItems.toList()
    }
}