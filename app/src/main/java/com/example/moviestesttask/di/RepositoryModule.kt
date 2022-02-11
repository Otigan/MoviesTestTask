package com.example.moviestesttask.di

import com.example.moviestesttask.data.repository.MoviesRepositoryImpl
import com.example.moviestesttask.domain.repository.MoviesRepository
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
    abstract fun bindMoviesRepository(impl: MoviesRepositoryImpl): MoviesRepository

}