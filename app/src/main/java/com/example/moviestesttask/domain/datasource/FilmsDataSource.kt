package com.example.moviestesttask.domain.datasource

import com.example.moviestesttask.data.model.Response


interface FilmsDataSource {

    suspend fun getMovies(): Response

}