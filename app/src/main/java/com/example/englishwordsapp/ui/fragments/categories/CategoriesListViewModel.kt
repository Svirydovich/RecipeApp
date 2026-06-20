package com.example.englishwordsapp.ui.fragments.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishwordsapp.data.repository.RecipesRepository
import com.example.englishwordsapp.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoriesListState(
    val categories: List<Category> = emptyList(),
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class CategoriesListViewModel @Inject constructor(private val repository: RecipesRepository) : ViewModel() {
    private val _state = MutableLiveData(CategoriesListState())
    val state: LiveData<CategoriesListState>
        get() = _state

    init {
        loadCategories()
    }

    private fun loadCategories() {
        _state.value = CategoriesListState(isLoading = true)

        viewModelScope.launch {
            val cached = repository.getCategoriesFromCacheOnce()
            if (cached.isNotEmpty()) {
                _state.value = CategoriesListState(categories = cached)
            }

            val remote = repository.getCategories()
            if (remote != null) {
                repository.saveCategoriesToCache(remote)
                _state.value = CategoriesListState(categories = remote)
            } else if (cached.isEmpty()) {
                _state.value = CategoriesListState(errorMessage = "Нет подключения")
            } else _state.value = _state.value?.copy(isLoading = false)
        }
    }
}
