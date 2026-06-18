package com.example.englishwordsapp.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import com.example.englishwordsapp.data.repository.RecipesRepository
import com.example.englishwordsapp.ui.fragments.recipes.favorites.FavoritesViewModel

class FavoritesViewModelFactory(private val recipesRepository: RecipesRepository) :
    Factory<FavoritesViewModel>, ViewModelProvider.Factory {
    override fun create(savedStateHandle: SavedStateHandle): FavoritesViewModel {
        return FavoritesViewModel(recipesRepository)
    }
}
