package com.example.moviestesttask.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestesttask.R
import com.example.moviestesttask.databinding.ItemGenreBinding
import com.example.moviestesttask.databinding.ItemHeaderBinding
import com.example.moviestesttask.domain.entity.GenreListItem
import com.example.moviestesttask.ui.adapter.util.ViewTypes.GENRE_HEADER_VIEW_TYPE
import com.example.moviestesttask.ui.adapter.util.ViewTypes.GENRE_VIEW_TYPE
import com.example.moviestesttask.ui.adapter.viewholder.GenreHeaderViewHolder
import com.google.android.material.color.MaterialColors


class GenreAdapter(
    private val onClick: (genre: GenreListItem.Genre) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var selectedPosition = RecyclerView.NO_POSITION

    var genres: List<GenreListItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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
                GenreHeaderViewHolder(binding)
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
            is GenreHeaderViewHolder -> {
                holder.bind(genres[position] as GenreListItem.Header)
            }
            is GenreViewHolder -> {
                holder.bind(genres[position] as GenreListItem.Genre)
                if (selectedPosition == holder.bindingAdapterPosition) {
                    val selected_color =
                        MaterialColors.getColor(
                            holder.itemView,
                            R.attr.selectedGenreColor
                        )
                    holder.itemView.setBackgroundColor(selected_color)
                } else {
                    val unselected_color =
                        MaterialColors.getColor(
                            holder.itemView,
                            R.attr.unselectedGenreColor
                        )
                    holder.itemView.setBackgroundColor(unselected_color)
                }
                holder.itemView.setOnClickListener {
                    if (selectedPosition == holder.bindingAdapterPosition) {
                        onClick(genres[holder.bindingAdapterPosition] as GenreListItem.Genre)
                        selectedPosition = -1;
                        notifyDataSetChanged();
                        return@setOnClickListener
                    }
                    onClick(genres[holder.bindingAdapterPosition] as GenreListItem.Genre)
                    selectedPosition = holder.bindingAdapterPosition;
                    notifyDataSetChanged();
                }
            }
        }
    }

    override fun getItemCount(): Int = genres.size
}
