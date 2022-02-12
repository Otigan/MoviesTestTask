package com.example.moviestesttask.presentation.util

import com.example.moviestesttask.domain.entity.FilmListItem

sealed class FilmEvent {
    data class Success(val films: List<FilmListItem>) :
        FilmEvent()

    object Loading : FilmEvent()
    data class Error(val errorMessage: String) : FilmEvent()
    object Empty : FilmEvent()
}