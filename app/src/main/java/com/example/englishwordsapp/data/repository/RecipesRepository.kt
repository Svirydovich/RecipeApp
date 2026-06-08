package com.example.englishwordsapp.data.repository

import android.content.Context
import com.example.englishwordsapp.data.api.RecipeApiService
import com.example.englishwordsapp.model.Category
import com.example.englishwordsapp.model.Recipe
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipesRepository(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "recipe_prefs"
        private const val FAVORITES_KEY = "favorites_set"
        const val BASE_URL = "https://recipes.androidsprint.ru/"
        const val IMAGES_PATH = "api/images/"
    }

    private val apiService: RecipeApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(RecipeApiService::class.java)
    }

    fun getCategories(): List<Category>? {
        return try {
            val response = apiService.getCategories().execute()

            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getCategoryById(categoryId: Int): Category? {
        return try {
            val response = apiService.getCategoryById(categoryId).execute()

            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return try {
            val response = apiService.getRecipesByCategory(categoryId).execute()

            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getRecipeById(recipeId: Int): Recipe? {
        return try {
            val response = apiService.getRecipeById(recipeId).execute()

            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getRecipesByIds(ids: Set<String>): List<Recipe>? {
        return try {
            val response = apiService.getRecipes().execute()

            if (response.isSuccessful) {
                response.body()?.filter {
                    ids.contains(it.id.toString())
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
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
