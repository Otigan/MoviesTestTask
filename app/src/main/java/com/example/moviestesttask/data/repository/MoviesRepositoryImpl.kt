package com.example.moviestesttask.data.repository

import com.example.moviestesttask.data.datasource.MoviesDataSource
import com.example.moviestesttask.data.util.Resource
import com.example.moviestesttask.data.util.ResponseHandler
import com.example.moviestesttask.domain.entity.Film
import com.example.moviestesttask.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesDataSource: MoviesDataSource,
    private val responseHandler: ResponseHandler
) :
    MoviesRepository {

    override fun getMovies(): Flow<Resource<List<Film>>> = flow {
        try {
            emit(Resource.Loading())
            val response = moviesDataSource.getMovies()
            val movies = response.films
            emit(responseHandler.handleSuccess(movies))
        } catch (e: Exception) {
            emit(responseHandler.handleException(e, null))
        }
    }

    override fun getMoviesByGenre(genre: String): Flow<Resource<List<Film>>> = flow {
        try {
            emit(Resource.Loading())
            val response = moviesDataSource.getMovies()
            val movies = if (genre.isEmpty()) {
                response.films
            } else {
                response.films.filter {
                    it.genres.contains(genre)
                }
            }
            emit(responseHandler.handleSuccess(movies))
        } catch (e: Exception) {
            emit(responseHandler.handleException(e, null))
        }
    }
}