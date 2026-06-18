package com.example.englishwordsapp.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import com.example.englishwordsapp.data.repository.RecipesRepository
import com.example.englishwordsapp.ui.fragments.categories.CategoriesListViewModel

class CategoryListViewModelFactory(private val recipesRepository: RecipesRepository) :
    Factory<CategoriesListViewModel>, ViewModelProvider.Factory {
    override fun create(savedStateHandle: SavedStateHandle): CategoriesListViewModel {
        return CategoriesListViewModel(recipesRepository)
    }
}
