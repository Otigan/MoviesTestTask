package com.example.moviestesttask.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.moviestesttask.databinding.ItemGenreBinding
import com.example.moviestesttask.domain.entity.GenreListItem

class GenreViewHolder(
    private val binding: ItemGenreBinding,
    private val onClick: (genre: GenreListItem.Genre) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(genre: GenreListItem.Genre) {
        binding.apply {
            root.setOnClickListener {
                onClick(genre)
            }
            txtGenreName.text = genre.title
        }
    }
}