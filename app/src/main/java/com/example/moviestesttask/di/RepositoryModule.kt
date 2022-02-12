package com.example.moviestesttask.di

import com.example.moviestesttask.data.repository.FilmsRepositoryImpl
import com.example.moviestesttask.domain.repository.FilmsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMoviesRepository(impl: FilmsRepositoryImpl): FilmsRepository

}