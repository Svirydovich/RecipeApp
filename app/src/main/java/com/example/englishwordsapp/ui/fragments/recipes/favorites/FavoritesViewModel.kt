package com.example.englishwordsapp.ui.fragments.recipes.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.englishwordsapp.data.repository.RecipesRepository
import com.example.englishwordsapp.model.Recipe
import kotlinx.coroutines.flow.map

data class FavoritesState(
    val recipes: List<Recipe> = emptyList(),
    val isEmpty: Boolean = true,
    val errorMessage: String? = null
)

class FavoritesViewModel(repository: RecipesRepository) : ViewModel() {
    val state: LiveData<FavoritesState> = repository.observeFavoriteRecipes()
        .map { recipes -> FavoritesState(recipes, recipes.isEmpty()) }
        .asLiveData(viewModelScope.coroutineContext)
}
