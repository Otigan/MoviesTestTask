package com.example.moviestesttask.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestesttask.R
import com.example.moviestesttask.databinding.ItemMovieBinding
import com.example.moviestesttask.domain.entity.FilmListItem

class FilmViewHolder(
    private val binding: ItemMovieBinding,
    private val onClick: (film: FilmListItem.Film) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(film: FilmListItem.Film) {
        binding.apply {
            root.setOnClickListener {
                onClick(film)
            }
            Glide.with(root.context)
                .load(film.image_url)
                .error(R.drawable.not_found)
                .into(cardImage)
            movieTitle.text = film.localized_name
        }
    }
}