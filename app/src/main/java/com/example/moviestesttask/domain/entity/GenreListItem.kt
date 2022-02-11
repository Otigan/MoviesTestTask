package com.example.moviestesttask.domain.entity

sealed class GenreListItem {

    data class Genre(val title: String) : GenreListItem()

    data class Header(
        val title: String
    ) : GenreListItem()
}