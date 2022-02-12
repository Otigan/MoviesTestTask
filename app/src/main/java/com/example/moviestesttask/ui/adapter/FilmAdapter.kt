package com.example.moviestesttask.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestesttask.databinding.ItemHeaderBinding
import com.example.moviestesttask.databinding.ItemMovieBinding
import com.example.moviestesttask.domain.entity.FilmListItem
import com.example.moviestesttask.ui.adapter.util.ViewTypes.FILM_HEADER_VIEW_TYPE
import com.example.moviestesttask.ui.adapter.util.ViewTypes.FILM_VIEW_TYPE
import com.example.moviestesttask.ui.adapter.viewholder.FilmHeaderViewHolder
import com.example.moviestesttask.ui.adapter.viewholder.FilmViewHolder

class FilmAdapter(private val onClick: (film: FilmListItem.Film) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var films: List<FilmListItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int =
        if (films[position] is FilmListItem.Header) {
            FILM_HEADER_VIEW_TYPE
        } else {
            FILM_VIEW_TYPE
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            FILM_HEADER_VIEW_TYPE -> {
                val binding =
                    ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FilmHeaderViewHolder(binding)
            }
            FILM_VIEW_TYPE -> {
                val binding =
                    ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FilmViewHolder(binding, onClick)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FilmHeaderViewHolder -> {
                holder.bind(films[position] as FilmListItem.Header)
            }
            is FilmViewHolder -> {
                holder.bind(films[position] as FilmListItem.Film)
            }
        }
    }

    override fun getItemCount(): Int = films.size
}