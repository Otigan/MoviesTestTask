package com.example.moviestesttask.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestesttask.databinding.ItemGenreBinding
import com.example.moviestesttask.databinding.ItemHeaderBinding
import com.example.moviestesttask.domain.entity.GenreListItem
import com.example.moviestesttask.ui.adapter.ViewTypes.GENRE_VIEW_TYPE
import com.example.moviestesttask.ui.adapter.ViewTypes.HEADER_VIEW_TYPE

class GenreAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var genres: List<GenreListItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun getItemViewType(position: Int): Int =
        if (genres[position] is GenreListItem.Header) {
            HEADER_VIEW_TYPE
        } else {
            GENRE_VIEW_TYPE
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            HEADER_VIEW_TYPE -> {
                val binding =
                    ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }
            GENRE_VIEW_TYPE -> {
                val binding =
                    ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GenreViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(genres[position] as GenreListItem.Header)
            }
            is GenreViewHolder -> {
                holder.bind(genres[position] as GenreListItem.Genre)
            }
        }
    }

    override fun getItemCount(): Int = genres.size
}