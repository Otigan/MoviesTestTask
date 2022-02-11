package com.example.moviestesttask.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestesttask.databinding.ItemMovieBinding
import com.example.moviestesttask.domain.entity.ListItem

class MovieViewHolder(private val binding: ItemMovieBinding) :
    RecyclerView.ViewHolder(binding.root) {



    fun bind(movie: ListItem.Film) {
        binding.apply {
            Glide.with(root.context)
                .load(movie.image_url)
                .into(cardImage)
            movieTitle.text = movie.localized_name
        }
    }
}