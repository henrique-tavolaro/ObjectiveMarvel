package com.example.marvelproject

import android.app.Application
import com.example.marvelproject.di.RepositoryModule.repositoryModule
import com.example.marvelproject.di.RetrofitModule.retrofitModule
import com.example.marvelproject.di.ViewModelModule.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BaseApplication)
            modules(
                listOf(
                    viewModelModule,
                    retrofitModule,
                    repositoryModule
                )
            )
        }
    }

}