package com.example.marvelproject.di

import com.example.marvelproject.presenter.MarvelViewModel
import org.koin.androidx.viewmodel.dsl.viewModel

import org.koin.dsl.module

object ViewModelModule {

    val viewModelModule = module {
        viewModel {
            MarvelViewModel(
                get()
            )
        }

    }

}