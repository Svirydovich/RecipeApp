package com.example.englishwordsapp.ui.fragments.recipes.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.englishwordsapp.data.repository.RecipesRepository
import com.example.englishwordsapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class FavoritesState(
    val recipes: List<Recipe> = emptyList(),
    val isEmpty: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(repository: RecipesRepository) : ViewModel() {
    val state: LiveData<FavoritesState> = repository.observeFavoriteRecipes()
        .map { recipes -> FavoritesState(recipes, recipes.isEmpty()) }
        .asLiveData(viewModelScope.coroutineContext)
}
