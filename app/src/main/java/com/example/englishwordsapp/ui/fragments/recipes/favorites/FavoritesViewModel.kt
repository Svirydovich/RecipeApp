package com.example.englishwordsapp.ui.fragments.recipes.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RecipesRepository(application)

    val state: LiveData<FavoritesState> = repository.observeFavoriteRecipes()
        .map { recipes -> FavoritesState(recipes, recipes.isEmpty()) }
        .asLiveData(viewModelScope.coroutineContext)
}
