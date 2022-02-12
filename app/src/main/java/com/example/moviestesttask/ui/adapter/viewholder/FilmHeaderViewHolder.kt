package com.example.moviestesttask.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.moviestesttask.databinding.ItemHeaderBinding
import com.example.moviestesttask.domain.entity.FilmListItem

class FilmHeaderViewHolder(private val binding: ItemHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(header: FilmListItem.Header) {
        binding.txtHeaderTitle.text = header.title
    }

}