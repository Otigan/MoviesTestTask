package com.example.moviestesttask.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviestesttask.data.util.Resource
import com.example.moviestesttask.domain.entity.Film
import com.example.moviestesttask.domain.entity.ListItem
import com.example.moviestesttask.domain.use_case.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MovieEvent {
    data class Success(val movies: List<ListItem>) : MovieEvent()
    object Loading : MovieEvent()
    data class Error(val errorMessage: String) : MovieEvent()
}

@HiltViewModel
class MoviesViewModel @Inject constructor(private val getMoviesUseCase: GetMoviesUseCase) :
    ViewModel() {

    private val moviesEventChannel = Channel<MovieEvent>(Channel.BUFFERED)
    val moviesEventFlow = moviesEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getMoviesUseCase().collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> {
                        resource.errorMessage?.let {
                            moviesEventChannel.send(MovieEvent.Error(it))
                        }
                    }
                    is Resource.Loading -> moviesEventChannel.send(MovieEvent.Loading)
                    is Resource.Success -> {
                        resource.data?.let {
                            val finalList = mergeLists(it)
                            moviesEventChannel.send(MovieEvent.Success(finalList))
                        }
                    }
                }
            }
        }
    }

    private fun mergeLists(movies: List<Film>): List<ListItem> {
        val genres = mutableListOf<String>()
        val finalList = mutableListOf<ListItem>()
        for (movie in movies) {
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