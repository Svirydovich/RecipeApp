package com.example.englishwordsapp.ui.fragments.recipes.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.englishwordsapp.data.RecipesRepository
import com.example.englishwordsapp.model.Recipe

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
        val favoriteIds = repository.getFavorites()
        val favoriteRecipes = repository.getRecipesByIds(favoriteIds)

        _state.value = FavoritesState(
            recipes = favoriteRecipes,
            isEmpty = favoriteRecipes.isEmpty()
        )
    }
}
