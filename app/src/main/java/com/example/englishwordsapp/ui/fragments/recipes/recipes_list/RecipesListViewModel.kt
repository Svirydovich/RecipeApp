package com.example.englishwordsapp.ui.fragments.recipes.recipes_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishwordsapp.data.repository.RecipesRepository
import com.example.englishwordsapp.di.AppModule.Companion.BASE_URL
import com.example.englishwordsapp.di.AppModule.Companion.IMAGES_PATH
import com.example.englishwordsapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipesListState(
    val recipes: List<Recipe> = emptyList(),
    val categoryName: String = "",
    val categoryImageUrl: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class RecipesListViewModel @Inject constructor(private val repository: RecipesRepository) : ViewModel() {
    private val _state = MutableLiveData(RecipesListState())

    val state: LiveData<RecipesListState>
        get() = _state

    fun loadRecipesByCategory(categoryId: Int, categoryName: String, categoryImageUrl: String) {
        viewModelScope.launch {
            val cached = repository.getRecipesByCategoryFromCacheOnce(categoryId)
            if (cached.isNotEmpty()) {
                val categoryUrl = "$BASE_URL$IMAGES_PATH$categoryImageUrl"
                _state.value = RecipesListState(recipes = cached, categoryName, categoryUrl)
            }

            val remote = repository.getRecipesByCategoryId(categoryId)
            if (remote != null) {
                repository.saveRecipesToCache(remote)
                val categoryUrl = "$BASE_URL$IMAGES_PATH$categoryImageUrl"
                _state.value = RecipesListState(recipes = remote, categoryName, categoryUrl)
            } else if (cached.isEmpty()) {
                _state.value = _state.value?.copy(errorMessage = "Нет подключения")
            }
        }
    }
}
