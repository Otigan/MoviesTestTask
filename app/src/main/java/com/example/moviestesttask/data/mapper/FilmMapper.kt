package com.example.moviestesttask.data.mapper

import com.example.moviestesttask.data.model.Film
import com.example.moviestesttask.domain.entity.FilmListItem
import com.example.moviestesttask.domain.mapper.Mapper

class FilmMapper : Mapper<Film, FilmListItem.Film> {

    override fun toEntity(model: Film): FilmListItem.Film =
        FilmListItem.Film(
            model.id,
            model.localized_name,
            model.name,
            model.year,
            model.rating,
            model.image_url,
            model.description,
            model.genres
        )

    override fun toModel(entity: FilmListItem.Film): Film =
        Film(
            entity.id,
            entity.localized_name,
            entity.name,
            entity.year,
            entity.rating,
            entity.image_url,
            entity.description,
            entity.genres
        )
}