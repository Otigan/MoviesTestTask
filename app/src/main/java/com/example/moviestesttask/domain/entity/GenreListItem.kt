package com.example.moviestesttask.domain.entity

sealed class GenreListItem {

    data class Genre(val title: String, var isSelected: Boolean = false) : GenreListItem()

    data class Header(
        val title: String
    ) : GenreListItem()
}