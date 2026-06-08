package com.example.englishwordsapp.ui.fragments.recipes.recipes_list

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.englishwordsapp.data.repository.RecipesRepository
import com.example.englishwordsapp.data.repository.RecipesRepository.Companion.BASE_URL
import com.example.englishwordsapp.data.repository.RecipesRepository.Companion.IMAGES_PATH
import com.example.englishwordsapp.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class RecipesListState(
    val recipes: List<Recipe> = emptyList(),
    val categoryName: String = "",
    val categoryImageUrl: String? = null
)

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableLiveData(RecipesListState())
    private val repository = RecipesRepository(application)

    val state: LiveData<RecipesListState>
        get() = _state

    fun loadRecipesByCategory(categoryId: Int, categoryName: String, categoryImageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipes = repository.getRecipesByCategoryId(categoryId)

            if (recipes == null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(getApplication(), "Ошибка получения данных", Toast.LENGTH_SHORT)
                        .show()
                }
                return@launch
            }

            val categoryUrl = "$BASE_URL$IMAGES_PATH$categoryImageUrl"

            withContext(Dispatchers.Main) {
                _state.value = RecipesListState(recipes, categoryName, categoryUrl)
            }
        }
    }
}
