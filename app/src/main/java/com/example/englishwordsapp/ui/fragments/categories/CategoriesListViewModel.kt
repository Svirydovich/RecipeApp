package com.example.englishwordsapp.ui.fragments.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.englishwordsapp.data.repository.RecipesRepository
import com.example.englishwordsapp.model.Category
import kotlinx.coroutines.launch

data class CategoriesListState(
    val categories: List<Category> = emptyList(),
    val errorMessage: String? = null
)

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableLiveData(CategoriesListState())
    private val repository = RecipesRepository(application)

    val state: LiveData<CategoriesListState>
        get() = _state

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _state.value = _state.value?.copy(errorMessage = null)

            val categories = repository.getCategories()
            if (categories == null) {
                _state.value = _state.value?.copy(errorMessage = "Ошибка получения данных")
                return@launch
            }
            _state.value = CategoriesListState(categories = categories)
        }
    }

    fun getCategoryById(categoryId: Int): Category? {
        return _state.value?.categories?.find { it.id == categoryId }
    }
}
