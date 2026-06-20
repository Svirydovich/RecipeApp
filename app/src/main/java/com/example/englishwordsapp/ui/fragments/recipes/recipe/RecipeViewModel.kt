package com.example.englishwordsapp.ui.fragments.recipes.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishwordsapp.data.repository.RecipesRepository
import com.example.englishwordsapp.di.AppModule.Companion.BASE_URL
import com.example.englishwordsapp.di.AppModule.Companion.IMAGES_PATH
import com.example.englishwordsapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeState(
    val recipe: Recipe? = null,
    val isFavorites: Boolean = false,
    val servings: Int = 1,
    val recipeImageUrl: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class RecipeViewModel @Inject constructor(private val repository: RecipesRepository) : ViewModel() {
    private val _recipeState = MutableLiveData(RecipeState())

    val recipeState: LiveData<RecipeState>
        get() = _recipeState

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            val recipe = repository.getRecipeById(recipeId)
            if (recipe == null) {
                _recipeState.value =
                    _recipeState.value?.copy(errorMessage = "Ошибка получения данных")
                return@launch
            }

            val favoriteRecipes = repository.observeFavoriteRecipes().first()
            val isFavorite = favoriteRecipes.any { it.id == recipeId }

            val imageUrl = "$BASE_URL$IMAGES_PATH${recipe.imageUrl}"
            _recipeState.value = RecipeState(
                recipe = recipe,
                isFavorites = isFavorite,
                servings = 1,
                recipeImageUrl = imageUrl
            )
        }
    }

    fun toggleFavorite() {
        val currentState = _recipeState.value ?: return
        val recipe = currentState.recipe ?: return
        val newIsFavorite = !currentState.isFavorites

        _recipeState.value = currentState.copy(isFavorites = newIsFavorite)

        viewModelScope.launch {
            if (newIsFavorite) repository.addToFavorites(recipe.id)
            else repository.removeFromFavorites(recipe.id)
        }
    }

    fun updatePortions(newServings: Int) {
        val currentState = _recipeState.value ?: return

        _recipeState.value = currentState.copy(servings = newServings)
    }
}
