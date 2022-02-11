package com.example.moviestesttask.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestesttask.databinding.ItemHeaderBinding
import com.example.moviestesttask.databinding.ItemMovieBinding
import com.example.moviestesttask.domain.entity.MovieListItem
import com.example.moviestesttask.ui.adapter.ViewTypes.FILM_VIEW_TYPE
import com.example.moviestesttask.ui.adapter.ViewTypes.HEADER_VIEW_TYPE

class MoviesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var films: List<MovieListItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int =
        if (films[position] is MovieListItem.Header) {
            HEADER_VIEW_TYPE
        } else {
            FILM_VIEW_TYPE
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            HEADER_VIEW_TYPE -> {
                val binding =
                    ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }
            FILM_VIEW_TYPE -> {
                val binding =
                    ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MovieViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(films[position] as MovieListItem.Header)
            }
            is MovieViewHolder -> {
                holder.bind(films[position] as MovieListItem.Film)
            }
        }
    }

    override fun getItemCount(): Int = films.size
}