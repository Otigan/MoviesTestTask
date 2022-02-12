package com.example.moviestesttask.domain.repository

import com.example.moviestesttask.data.model.Film
import com.example.moviestesttask.data.util.Resource
import com.example.moviestesttask.domain.entity.FilmListItem
import com.example.moviestesttask.domain.entity.GenreListItem
import kotlinx.coroutines.flow.Flow

interface FilmsRepository {

    fun getMovies(): Flow<Resource<List<FilmListItem>>>

    fun getMoviesByGenre(genre: String): Flow<Resource<List<FilmListItem>>>

    fun getGenres(): Flow<Resource<List<GenreListItem>>>

}