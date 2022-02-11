package com.example.moviestesttask.domain.repository

import com.example.moviestesttask.data.util.Resource
import com.example.moviestesttask.domain.entity.Film
import com.example.moviestesttask.domain.entity.ListItem
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getMovies(): Flow<Resource<List<Film>>>

}