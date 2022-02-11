package com.example.moviestesttask.domain.repository

import com.example.moviestesttask.data.util.Resource
import com.example.moviestesttask.domain.entity.Film
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getMovies(): Flow<Resource<List<Film>>>

    fun getMoviesByGenre(genre: String): Flow<Resource<List<Film>>>

}