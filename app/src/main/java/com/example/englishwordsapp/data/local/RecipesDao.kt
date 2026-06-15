package com.example.englishwordsapp.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.englishwordsapp.model.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipes WHERE categoryId = :categoryId")
    fun getRecipesByCategory(categoryId: Int): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE categoryId = :categoryId")
    suspend fun getAllOnce(categoryId: Int): List<Recipe>

    @Upsert
    suspend fun insertRecipes(recipes: List<Recipe>)

    @Query("SELECT * FROM recipes WHERE isFavorite = 1")
    fun getFavoriteRecipes(): Flow<List<Recipe>>

    @Query("UPDATE recipes SET isFavorite = :isFavorite WHERE id = :recipeId")
    suspend fun updateFavorite(recipeId: Int, isFavorite: Boolean)
}
