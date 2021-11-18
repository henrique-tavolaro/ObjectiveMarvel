package com.example.marvelproject.di

import com.example.marvelproject.datasource.RetrofitService
import com.example.marvelproject.domain.repository.MarvelRepository
import com.example.marvelproject.repository.MarvelRepositoryImpl
import org.koin.dsl.module

object RepositoryModule {

    val repositoryModule = module {
        single { provideRepository(get()) }
    }

    private fun provideRepository(
        retrofitService: RetrofitService
    ): MarvelRepository {
        return MarvelRepositoryImpl(retrofitService)
    }
}