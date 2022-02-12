package com.example.moviestesttask.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestesttask.databinding.ItemGenreBinding
import com.example.moviestesttask.databinding.ItemHeaderBinding
import com.example.moviestesttask.domain.entity.GenreListItem
import com.example.moviestesttask.ui.adapter.ViewTypes.GENRE_HEADER_VIEW_TYPE
import com.example.moviestesttask.ui.adapter.ViewTypes.GENRE_VIEW_TYPE

class GenreAdapter(
    private val onClick: (genre: GenreListItem.Genre) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selected_position = -1

    var genres: List<GenreListItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var isSelected = false

    inner class GenreViewHolder(
        private val binding: ItemGenreBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: GenreListItem.Genre) {
            binding.apply {
                txtGenreName.text = genre.title
            }
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (genres[position] is GenreListItem.Header) {
            GENRE_HEADER_VIEW_TYPE
        } else {
            GENRE_VIEW_TYPE
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            GENRE_HEADER_VIEW_TYPE -> {
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
                if (selected_position == holder.bindingAdapterPosition) {
                    holder.itemView.setBackgroundColor(Color.parseColor("#FFBB86FC"))
                } else {
                    holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
                }
                holder.itemView.setOnClickListener {
                    if (selected_position == holder.bindingAdapterPosition) {
                        onClick(genres[holder.bindingAdapterPosition] as GenreListItem.Genre)
                        selected_position = -1;
                        notifyDataSetChanged();
                        return@setOnClickListener
                    }
                    onClick(genres[holder.bindingAdapterPosition] as GenreListItem.Genre)
                    selected_position = holder.bindingAdapterPosition;
                    notifyDataSetChanged();
                }
            }
        }
    }

    override fun getItemCount(): Int = genres.size
}

interface OnItemClickListener {
    fun onItemClick(position: Int)
}