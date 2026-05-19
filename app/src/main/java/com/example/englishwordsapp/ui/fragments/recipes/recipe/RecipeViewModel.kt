package com.example.englishwordsapp.ui.fragments.recipes.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.englishwordsapp.model.Recipe

data class RecipeState(
    val recipe: Recipe? = null,
    val isFavorites: Boolean = false,
    val servings: Int = 1
)

class RecipeViewModel : ViewModel() {
    private val _recipeState = MutableLiveData(RecipeState())

    val recipeState: LiveData<RecipeState>
        get() = _recipeState

    fun toggleFavorite() {
        val currentState = _recipeState.value ?: RecipeState()
        val newState = currentState.copy(isFavorites = !currentState.isFavorites)
        _recipeState.value = newState
    }

    fun initializeRecipe(recipe: Recipe, isFavorite: Boolean) {
        _recipeState.value = RecipeState(recipe = recipe, isFavorites = isFavorite)
    }
}
