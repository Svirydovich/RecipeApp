package com.example.englishwordsapp.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import com.example.englishwordsapp.data.repository.RecipesRepository
import com.example.englishwordsapp.ui.fragments.recipes.recipes_list.RecipesListViewModel

class RecipesListViewModelFactory(private val recipesRepository: RecipesRepository) :
    Factory<RecipesListViewModel>, ViewModelProvider.Factory {
    override fun create(savedStateHandle: SavedStateHandle): RecipesListViewModel {
        return RecipesListViewModel(recipesRepository)
    }
}
