package com.example.moviestesttask.data.repository

import com.example.moviestesttask.data.datasource.FilmsDataSourceImpl
import com.example.moviestesttask.data.mapper.FilmMapper
import com.example.moviestesttask.data.util.Resource
import com.example.moviestesttask.data.util.ResponseHandler
import com.example.moviestesttask.domain.entity.FilmListItem
import com.example.moviestesttask.domain.entity.GenreListItem
import com.example.moviestesttask.domain.repository.FilmsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FilmsRepositoryImpl @Inject constructor(
    private val filmsDataSourceImpl: FilmsDataSourceImpl,
    private val responseHandler: ResponseHandler
) :
    FilmsRepository {

    override fun getGenres(): Flow<Resource<List<GenreListItem>>> = flow {
        try {
            emit(Resource.Loading())
            val response = filmsDataSourceImpl.getMovies()
            val films = response.films.sortedBy { it.localized_name }
            val genres = mutableListOf<String>()
            for (film in films) {
                genres.addAll(film.genres)
            }
            val distinctGenres = genres.distinct().map {
                GenreListItem.Genre(it)
            }
            val listItems =
                mutableListOf<GenreListItem>(GenreListItem.Header("Жанры"))
            listItems.addAll(distinctGenres)
            emit(responseHandler.handleSuccess(listItems.toList()))
        } catch (e: Exception) {
            emit(responseHandler.handleException(e, null))
        }
    }

    override fun getMovies(genre: String?): Flow<Resource<List<FilmListItem>>> = flow {
        try {
            emit(Resource.Loading())
            val response = filmsDataSourceImpl.getMovies()
            val films = when {
                genre?.isBlank() == true -> {
                    response.films.sortedBy { it.localized_name }
                }
                else -> {
                    response.films.filter {
                        it.genres.contains(genre)
                    }
                }
            }
            val mapper = FilmMapper()
            val filmItems = films.map {
                mapper.toEntity(it)
            }
            val listItems =
                mutableListOf<FilmListItem>(FilmListItem.Header("Фильмы"))
            listItems.addAll(filmItems)
            emit(responseHandler.handleSuccess(listItems.toList()))
        } catch (e: Exception) {
            emit(responseHandler.handleException(e, null))
        }
    }

}