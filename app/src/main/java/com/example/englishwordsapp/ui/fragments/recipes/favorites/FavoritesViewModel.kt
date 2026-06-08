package com.example.englishwordsapp.ui.fragments.recipes.favorites

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.englishwordsapp.data.repository.RecipesRepository
import com.example.englishwordsapp.model.Recipe
import kotlinx.coroutines.launch

data class FavoritesState(
    val recipes: List<Recipe> = emptyList(),
    val isEmpty: Boolean = true
)

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableLiveData(FavoritesState())
    private val repository = RecipesRepository(application)

    val state: LiveData<FavoritesState>
        get() = _state

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            val favoriteIds = repository.getFavorites()
            val favoriteRecipes = repository.getRecipesByIds(favoriteIds)

            if (favoriteRecipes == null) {
                Toast.makeText(getApplication(), "Ошибка получения данных", Toast.LENGTH_SHORT)
                    .show()
                return@launch
            }

            _state.value = FavoritesState(favoriteRecipes, favoriteRecipes.isEmpty())
        }
    }
}
