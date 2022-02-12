package com.example.moviestesttask.domain.use_case

import com.example.moviestesttask.domain.repository.FilmsRepository
import javax.inject.Inject

class GetGenresUseCase @Inject constructor(private val filmsRepository: FilmsRepository) {

    operator fun invoke() = filmsRepository.getGenres()

}