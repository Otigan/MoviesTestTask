package com.example.moviestesttask.presentation.util

import com.example.moviestesttask.domain.entity.GenreListItem

sealed class GenreEvent {
    data class Success(val genres: List<GenreListItem>) :
        GenreEvent()

    object Loading : GenreEvent()
    data class Error(val errorMessage: String) : GenreEvent()
    object Empty : GenreEvent()
}