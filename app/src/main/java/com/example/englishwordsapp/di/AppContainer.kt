package com.example.englishwordsapp.di

import android.app.Application
import androidx.room.Room
import com.example.englishwordsapp.data.api.RecipeApiService
import com.example.englishwordsapp.data.local.AppDatabase
import com.example.englishwordsapp.data.repository.RecipesRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(private val application: Application) {

    companion object {
        const val BASE_URL = "https://recipes.androidsprint.ru/"
        const val IMAGES_PATH = "api/images/"
    }

    val apiService: RecipeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecipeApiService::class.java)
    }

    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "recipe-database"
        ).fallbackToDestructiveMigration(false).build()
    }

    val recipesRepository: RecipesRepository by lazy {
        RecipesRepository(apiService, database)
    }
}
