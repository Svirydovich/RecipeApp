package com.example.englishwordsapp.ui.fragments.recipes.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.englishwordsapp.data.RecipesRepository
import com.example.englishwordsapp.model.Recipe

data class RecipeState(
    val recipe: Recipe? = null,
    val isFavorites: Boolean = false,
    val servings: Int = 1
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

            _recipeState.value = RecipeState(
                recipe = recipe,
                isFavorites = isFavorite,
                servings = _recipeState.value?.servings ?: 1
            )
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
        val newState = currentState.copy(isFavorites = !currentState.isFavorites)
        _recipeState.value = newState
    }

    private fun saveFavorites(recipeId: Int, isFavorite: Boolean) {
        val prefs = getApplication<Application>().getSharedPreferences("favorites",0)
        val favorites = (prefs.getStringSet("favorite_recipes", emptySet()) ?: emptySet()).toMutableSet()

        if (isFavorite) {
            favorites.add(recipeId.toString())
        } else {
            favorites.remove(recipeId.toString())
        }

        prefs.edit().putStringSet("favorite_recipes", favorites).apply()
    }
    fun initializeRecipe(recipe: Recipe, isFavorite: Boolean) {
        _recipeState.value = RecipeState(recipe = recipe, isFavorites = isFavorite)
    }
}
