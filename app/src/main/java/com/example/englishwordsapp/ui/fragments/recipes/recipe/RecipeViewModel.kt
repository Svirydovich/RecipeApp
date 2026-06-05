package com.example.englishwordsapp.ui.fragments.recipes.recipe

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.englishwordsapp.data.repository.RecipesRepository
import com.example.englishwordsapp.model.Recipe
import androidx.core.content.edit

data class RecipeState(
    val recipe: Recipe? = null,
    val isFavorites: Boolean = false,
    val servings: Int = 1,
    val recipeImage: Drawable? = null
)

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipeState = MutableLiveData(RecipeState())
    private val repository = RecipesRepository(application)

    val recipeState: LiveData<RecipeState>
        get() = _recipeState

    fun loadRecipe(recipeId: Int) {
        val recipe = repository.getRecipeById(recipeId)

        if (recipe != null) {
            val isFavorite = getFavorites().contains(recipe.id.toString())
            val recipeImage = loadImageFromAssets(recipe.imageUrl)

            _recipeState.value = RecipeState(
                recipe = recipe,
                isFavorites = isFavorite,
                servings = _recipeState.value?.servings ?: 1,
                recipeImage = recipeImage
            )
            // TODO: load from network
        }

    }

    private fun loadImageFromAssets(fileName: String): Drawable? {
        return try {
            val inputStream = getApplication<Application>().assets.open(fileName)
            Drawable.createFromStream(inputStream, null)
        } catch (e: Exception) {
            android.util.Log.e("RecipeViewModel", "Failed to load image: $fileName", e)
            null
        }
    }

    private fun getFavorites(): Set<String> {
        val prefs = getApplication<Application>().getSharedPreferences(
            "favorites",
            0
        )
        return prefs.getStringSet("favorite_recipes", emptySet()) ?: emptySet()
    }

    fun toggleFavorite() {
        val currentState = _recipeState.value ?: RecipeState()
        val recipe = currentState.recipe ?: return
        val newIsFavorite = !currentState.isFavorites

        _recipeState.value = currentState.copy(isFavorites = newIsFavorite)
        saveFavorites(recipe.id, newIsFavorite)
    }

    private fun saveFavorites(recipeId: Int, isFavorite: Boolean) {
        val prefs = getApplication<Application>().getSharedPreferences("favorites", 0)
        val favorites =
            (prefs.getStringSet("favorite_recipes", emptySet()) ?: emptySet()).toMutableSet()

        if (isFavorite) {
            favorites.add(recipeId.toString())
        } else {
            favorites.remove(recipeId.toString())
        }

        prefs.edit { putStringSet("favorite_recipes", favorites) }
    }

    fun updatePortions(newServings: Int) {
        val currentState = _recipeState.value ?: return

        _recipeState.value = currentState.copy(servings = newServings)
    }
}
