package com.example.englishwordsapp.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.englishwordsapp.data.api.RecipeApiService
import com.example.englishwordsapp.data.local.AppDatabase
import com.example.englishwordsapp.data.local.CategoriesDao
import com.example.englishwordsapp.data.local.RecipesDao
import com.example.englishwordsapp.model.Category
import com.example.englishwordsapp.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
    private val db: AppDatabase
    private val categoriesDao: CategoriesDao
    private val recipesDao: RecipesDao

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(RecipeApiService::class.java)

        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "recipe-database"
        ).build()

        categoriesDao = db.categoriesDao()
        recipesDao = db.recipesDao()
    }

    suspend fun getCategories(): List<Category>? = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getCategories().execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getCategoryById(categoryId: Int): Category? = withContext(Dispatchers.IO) {
        try {
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

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? =
        withContext(Dispatchers.IO) {
            try {
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

    suspend fun getRecipeById(recipeId: Int): Recipe? = withContext(Dispatchers.IO) {
        try {
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

    suspend fun getRecipesByIds(ids: Set<String>): List<Recipe>? = withContext(Dispatchers.IO) {
        try {
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

    suspend fun getCategoriesFromCacheOnce(): List<Category> = categoriesDao.getAllOnce()

    suspend fun getRecipesByCategoryFromCacheOnce(categoryId: Int): List<Recipe> =
        recipesDao.getAllOnce(categoryId)

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

    fun getCategoriesFromCache(): LiveData<List<Category>> = categoriesDao.getAll()

    suspend fun saveCategoriesToCache(categories: List<Category>) {
        categoriesDao.insertAll(categories)
    }

    suspend fun saveRecipesToCache(recipes: List<Recipe>) {
        recipesDao.insertRecipes(recipes)
    }
}
