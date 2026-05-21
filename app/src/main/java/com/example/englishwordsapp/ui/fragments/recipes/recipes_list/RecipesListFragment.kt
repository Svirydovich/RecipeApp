package com.example.englishwordsapp.ui.fragments.recipes.recipes_list

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.englishwordsapp.R
import com.example.englishwordsapp.data.RecipesRepository
import com.example.englishwordsapp.databinding.FragmentRecipesListBinding
import com.example.englishwordsapp.ui.adapters.RecipeAdapter
import com.example.englishwordsapp.ui.fragments.categories.CategoriesListFragment
import com.example.englishwordsapp.ui.fragments.recipes.recipe.RecipeFragment

class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding!!

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null
    private lateinit var repository: RecipesRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        repository = RecipesRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryId = arguments?.getInt(CategoriesListFragment.ARG_CATEGORY_ID)
        categoryName = arguments?.getString(CategoriesListFragment.ARG_CATEGORY_NAME)
        categoryImageUrl = arguments?.getString(CategoriesListFragment.ARG_CATEGORY_IMAGE_URL)

        binding.tvRecipesTitle.text = categoryName
        categoryImageUrl?.let { loadImageFromAssets(binding.ivCategoryImage, it) }

        val recipes = repository.getRecipesByCategoryId(categoryId ?: 0)

        val recipeAdapter = RecipeAdapter(recipes, { recipeId ->
            openRecipeByRecipeId(recipeId)
        }, requireContext())

        binding.rvRecipes.apply {
            adapter = recipeAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun loadImageFromAssets(imageView: ImageView, imageName: String) {
        try {
            val inputStream = requireContext().assets.open(imageName)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            imageView.setImageBitmap(bitmap)
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = Bundle().apply {
            putInt(RecipeFragment.ARG_RECIPE_ID, recipeId)
        }

        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            addToBackStack(null)
        }
    }
}
