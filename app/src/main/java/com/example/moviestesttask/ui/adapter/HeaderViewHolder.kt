package com.example.moviestesttask.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.moviestesttask.databinding.ItemHeaderBinding
import com.example.moviestesttask.domain.entity.GenreListItem
import com.example.moviestesttask.domain.entity.MovieListItem

class HeaderViewHolder(private val binding: ItemHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(header: MovieListItem.Header) {
        binding.txtHeaderTitle.text = header.title
    }

    fun bind(header: GenreListItem.Header) {
        binding.txtHeaderTitle.text = header.title
    }

}