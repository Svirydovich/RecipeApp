package com.example.englishwordsapp.data.repository

import android.content.Context
import com.example.englishwordsapp.data.api.RecipeApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipesRepository(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "recipe_prefs"
        private const val FAVORITES_KEY = "favorites_set"
    }

    private val apiService: RecipeApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://recipes.androidsprint.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(RecipeApiService::class.java)
    }

    fun getFavorites(): MutableSet<String> {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val favoritesSet = sharedPrefs.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        return HashSet(favoritesSet)
    }

    fun addToFavorites(recipeId: Int) {
        val currentFavorites = getFavorites()
        currentFavorites.add(recipeId.toString())
        saveFavorites(currentFavorites)
    }

    fun removeFromFavorites(recipeId: Int) {
        val currentFavorites = getFavorites()
        currentFavorites.remove(recipeId.toString())
        saveFavorites(currentFavorites)
    }

    private fun saveFavorites(favorites: Set<String>) {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit().apply {
            putStringSet(FAVORITES_KEY, favorites)
            apply()
        }
    }
}
