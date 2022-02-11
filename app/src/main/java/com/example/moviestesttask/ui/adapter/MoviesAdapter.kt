package com.example.moviestesttask.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestesttask.databinding.ItemGenreBinding
import com.example.moviestesttask.databinding.ItemMovieBinding
import com.example.moviestesttask.domain.entity.ListItem

class MoviesAdapter(
    private val setLinearLayoutManager: () -> Unit,
    private val setGridLayoutManager: () -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<ListItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private companion object {
        const val GENRE_VIEW_TYPE = 0
        const val MOVIE_VIEW_TYPE = 1
    }


    override fun getItemViewType(position: Int): Int =
        if (items[position] is ListItem.Genre) {
            setLinearLayoutManager()
            GENRE_VIEW_TYPE
        } else {
            setGridLayoutManager()
            MOVIE_VIEW_TYPE
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            GENRE_VIEW_TYPE -> {
                Log.d("MoviesAdapter", "onBindViewHolder: create genre holder")
                val binding =
                    ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GenreViewHolder(binding)
            }
            MOVIE_VIEW_TYPE -> {
                Log.d("MoviesAdapter", "onBindViewHolder: create movie holder")
                val binding =
                    ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MovieViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GenreViewHolder -> {
                Log.d("MoviesAdapter", "onBindViewHolder: bind genre")
                holder.bind(items[position] as ListItem.Genre)
            }
            is MovieViewHolder -> {
                Log.d("MoviesAdapter", "onBindViewHolder: bind movie")
                holder.bind(items[position] as ListItem.Film)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}