package com.example.moviestesttask.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.moviestesttask.databinding.ItemGenreBinding
import com.example.moviestesttask.domain.entity.ListItem

class GenreViewHolder(
    private val binding: ItemGenreBinding,
    private val onGenreClick: (genre: ListItem.Genre) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(genre: ListItem.Genre) {
        binding.apply {
            root.setOnClickListener {
                onGenreClick(genre)
            }
            txtGenreName.text = genre.name
        }
    }
}