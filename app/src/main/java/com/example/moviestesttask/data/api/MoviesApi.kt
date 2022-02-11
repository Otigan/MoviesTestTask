package com.example.moviestesttask.data.api

import com.example.moviestesttask.domain.entity.Response
import retrofit2.http.GET

interface MoviesApi {

    companion object {
        const val BASE_URL = "https://s3-eu-west-1.amazonaws.com/"
    }

    @GET("sequeniatesttask/films.json")
    suspend fun getMovies(): Response
}