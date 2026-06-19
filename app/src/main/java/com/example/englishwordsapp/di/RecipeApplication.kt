package com.example.englishwordsapp.di

import android.app.Application
import com.example.englishwordsapp.di.AppContainer

class RecipeApplication : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}
