package com.example.moviestesttask.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class MovieListItem {

    @Parcelize
    data class Film(
        val id: Int,
        val localized_name: String,
        val name: String,
        val year: Int,
        val rating: Double? = 0.0,
        val image_url: String? = "",
        val description: String? = "",
        val genres: List<String>
    ) : MovieListItem(), Parcelable

    data class Header(
        val title: String
    ) : MovieListItem()
}