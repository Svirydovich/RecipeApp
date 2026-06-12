package com.example.englishwordsapp.ui.fragments.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.englishwordsapp.data.repository.RecipesRepository
import com.example.englishwordsapp.model.Category
import kotlinx.coroutines.launch

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RecipesRepository(application)
    val categories: LiveData<List<Category>> = repository.getCategoriesFromCache()

    init {
        viewModelScope.launch {
            val remote = repository.getCategories()
            if (remote != null) {
                repository.saveCategoriesToCache(remote)
            }
        }
    }

    fun getCategoryById(categoryId: Int, categoriesList: List<Category>?): Category? {
        return categoriesList?.find { it.id == categoryId }
    }
}
