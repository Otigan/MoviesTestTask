package com.example.moviestesttask.ui.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestesttask.databinding.ItemGenreBinding
import com.example.moviestesttask.databinding.ItemMovieBinding
import com.example.moviestesttask.domain.entity.ListItem

class MoviesAdapter(
    private val onGenreClick: (genre: ListItem.Genre) -> Unit,
    private val onFilmClick: (film: ListItem.Film) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private lateinit var filtered: List<ListItem.Film>


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString() ?: ""
                if (query.isEmpty()) {
                    return FilterResults().apply { values = items }
                } else {
                    val movies = items.filterIsInstance<ListItem.Film>()
                    filtered = movies.filter {
                        it.genres.contains(query)
                    }
                }
                return FilterResults().apply { values = filtered }
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val items = items.toMutableList()
                items.removeIf {
                    it is ListItem.Film
                }
                val films = results?.values as MutableList<ListItem>
                items.addAll(films)
                this@MoviesAdapter.items = items
            }
        }
    }

    var items: List<ListItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    companion object {
        const val GENRE_VIEW_TYPE = 0
        const val MOVIE_VIEW_TYPE = 1
    }


    override fun getItemViewType(position: Int): Int =
        if (items[position] is ListItem.Genre) {
            GENRE_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            GENRE_VIEW_TYPE -> {
                Log.d("MoviesAdapter", "onBindViewHolder: create genre holder")
                val binding =
                    ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GenreViewHolder(binding, onGenreClick)
            }
            MOVIE_VIEW_TYPE -> {
                Log.d("MoviesAdapter", "onBindViewHolder: create movie holder")
                val binding =
                    ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MovieViewHolder(binding, onFilmClick)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GenreViewHolder -> {
                holder.bind(items[position] as ListItem.Genre)
            }
            is MovieViewHolder -> {
                holder.bind(items[position] as ListItem.Film)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}