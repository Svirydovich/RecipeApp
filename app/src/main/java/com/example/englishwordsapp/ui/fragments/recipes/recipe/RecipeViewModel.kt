package com.example.englishwordsapp.ui.fragments.recipes.recipe

import androidx.lifecycle.ViewModel
import com.example.englishwordsapp.model.Recipe

data class RecipeState(
    val recipe: Recipe? = null,
    val isFavorites: Boolean = false,
    val servings: Int = 1
)

class RecipeViewModel: ViewModel() {
    private var recipeState = RecipeState()
}
