package com.example.moviestesttask.domain.use_case

import com.example.moviestesttask.domain.repository.MoviesRepository
import javax.inject.Inject

class FilterMoviesUseCase @Inject constructor(private val moviesRepository: MoviesRepository) {


    operator fun invoke(genre: String) = moviesRepository.getMoviesByGenre(genre)
}