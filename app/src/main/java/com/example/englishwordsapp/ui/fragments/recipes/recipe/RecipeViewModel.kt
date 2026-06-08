package com.example.englishwordsapp.ui.fragments.recipes.recipe

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.englishwordsapp.data.repository.RecipesRepository
import com.example.englishwordsapp.model.Recipe
import androidx.core.content.edit
import androidx.lifecycle.viewModelScope
import com.example.englishwordsapp.data.repository.RecipesRepository.Companion.BASE_URL
import com.example.englishwordsapp.data.repository.RecipesRepository.Companion.IMAGES_PATH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class RecipeState(
    val recipe: Recipe? = null,
    val isFavorites: Boolean = false,
    val servings: Int = 1,
    val recipeImageUrl: String? = null
)

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipeState = MutableLiveData(RecipeState())
    private val repository = RecipesRepository(application)

    val recipeState: LiveData<RecipeState>
        get() = _recipeState

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipe = repository.getRecipeById(recipeId)

            if (recipe == null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(getApplication(), "Ошибка получения данных", Toast.LENGTH_SHORT)
                        .show()
                }
                return@launch
            }

            val isFavorite = repository.getFavorites().contains(recipe.id.toString())

            val imageUrl = "$BASE_URL$IMAGES_PATH${recipe.imageUrl}"

            withContext(Dispatchers.Main) {
                _recipeState.value = RecipeState(
                    recipe = recipe,
                    isFavorites = isFavorite,
                    servings = _recipeState.value?.servings ?: 1,
                    recipeImageUrl = imageUrl
                )
            }
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
