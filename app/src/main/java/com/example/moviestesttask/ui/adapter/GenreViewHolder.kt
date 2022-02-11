package com.example.moviestesttask.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.moviestesttask.databinding.ItemGenreBinding
import com.example.moviestesttask.domain.entity.GenreListItem

class GenreViewHolder(
    private val binding: ItemGenreBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(genre: GenreListItem.Genre) {
        binding.apply {
            txtGenreName.text = genre.title
        }
    }
}