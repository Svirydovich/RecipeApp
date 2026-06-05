package com.example.englishwordsapp.ui.fragments.recipes.recipes_list

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.englishwordsapp.data.repository.RecipesRepository
import com.example.englishwordsapp.model.Recipe

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
        val recipes = repository.getRecipesByCategoryId(categoryId)
        val categoryImage = loadImageFromAssets(categoryImageUrl)

        _state.value = RecipesListState(
            recipes = recipes,
            categoryName = categoryName,
            categoryImage = categoryImage
        )
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
