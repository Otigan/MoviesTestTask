package com.example.moviestesttask.data.datasource

import com.example.moviestesttask.data.api.MoviesApi
import com.example.moviestesttask.domain.datasource.FilmsDataSource
import javax.inject.Inject

class FilmsDataSourceImpl @Inject constructor(private val api: MoviesApi) : FilmsDataSource {

    override suspend fun getMovies() = api.getMovies()


}