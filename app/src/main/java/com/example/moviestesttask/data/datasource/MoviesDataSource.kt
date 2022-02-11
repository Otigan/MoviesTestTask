package com.example.moviestesttask.data.datasource

import com.example.moviestesttask.data.api.MoviesApi
import javax.inject.Inject

class MoviesDataSource @Inject constructor(private val api: MoviesApi) {

    suspend fun getMovies() = api.getMovies()

}