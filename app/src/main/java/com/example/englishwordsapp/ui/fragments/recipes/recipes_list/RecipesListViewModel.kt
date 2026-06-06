package com.example.englishwordsapp.ui.fragments.recipes.recipes_list

import android.app.Application
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.englishwordsapp.data.repository.RecipesRepository
import com.example.englishwordsapp.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class RecipesListState(
    val recipes: List<Recipe> = emptyList(),
    val categoryName: String = "",
    val categoryImage: Drawable? = null
)

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableLiveData(RecipesListState())
    private val repository = RecipesRepository(application)

    val state: LiveData<RecipesListState>
        get() = _state

    fun loadRecipesByCategory(categoryId: Int, categoryName: String, categoryImageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipes = repository.getRecipesByCategoryId(categoryId)

            withContext(Dispatchers.Main) {
                if (recipes == null) {
                    Toast.makeText(getApplication(), "Ошибка получения данных", Toast.LENGTH_SHORT)
                        .show()
                    return@withContext
                }
            }

            val categoryImage: Drawable? = loadImageFromAssets(categoryImageUrl)

            withContext(Dispatchers.Main) {
                _state.value = RecipesListState(
                    recipes = recipes!!,
                    categoryName = categoryName,
                    categoryImage = categoryImage
                )
            }
        }
    }

    private fun loadImageFromAssets(fileName: String): Drawable? {
        return try {
            val inputStream = getApplication<Application>().assets.open(fileName)
            Drawable.createFromStream(inputStream, null)
        } catch (e: Exception) {
            android.util.Log.e("RecipesListViewModel", "Failed to load image: $fileName", e)
            null
        }
    }
}
