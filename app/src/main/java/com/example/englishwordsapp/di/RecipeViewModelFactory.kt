package com.example.englishwordsapp.di

import androidx.lifecycle.SavedStateHandle
import com.example.englishwordsapp.data.repository.RecipesRepository
import com.example.englishwordsapp.ui.fragments.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(private val recipesRepository: RecipesRepository) :
    Factory<RecipeViewModel> {
    override fun create(savedStateHandle: SavedStateHandle): RecipeViewModel {
        return RecipeViewModel(recipesRepository)
    }
}
