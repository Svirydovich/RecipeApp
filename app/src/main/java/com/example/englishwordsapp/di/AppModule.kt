package com.example.englishwordsapp.di

import android.app.Application
import androidx.room.Room
import com.example.englishwordsapp.data.api.RecipeApiService
import com.example.englishwordsapp.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule() {
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @Provides
    @Singleton
    fun provideApiService(): RecipeApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RecipeApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "recipe-database"
    ).fallbackToDestructiveMigration(false).build()

    companion object {
        const val BASE_URL = "https://recipes.androidsprint.ru/"
        const val IMAGES_PATH = "api/images/"
    }
}
