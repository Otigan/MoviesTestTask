package com.example.moviestesttask.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.moviestesttask.databinding.ItemHeaderBinding
import com.example.moviestesttask.domain.entity.GenreListItem

class GenreHeaderViewHolder(private val binding: ItemHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(header: GenreListItem.Header) {
        binding.txtHeaderTitle.text = header.title
    }

}