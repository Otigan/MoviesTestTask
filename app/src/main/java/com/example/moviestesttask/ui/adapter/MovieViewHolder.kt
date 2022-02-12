package com.example.moviestesttask.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestesttask.databinding.ItemMovieBinding
import com.example.moviestesttask.domain.entity.MovieListItem

class MovieViewHolder(
    private val binding: ItemMovieBinding,
    private val onClick: (film: MovieListItem.Film) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: MovieListItem.Film) {
        binding.apply {
            root.setOnClickListener {
                onClick(movie)
            }
            Glide.with(root.context)
                .load(movie.image_url)
                .into(cardImage)
            movieTitle.text = movie.localized_name
        }
    }
}