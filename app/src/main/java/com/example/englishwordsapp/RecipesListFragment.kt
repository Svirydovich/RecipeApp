package com.example.englishwordsapp

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
import com.example.englishwordsapp.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding!!

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryId = arguments?.getInt(CategoriesListFragment.ARG_CATEGORY_ID)
        categoryName = arguments?.getString(CategoriesListFragment.ARG_CATEGORY_NAME)
        categoryImageUrl = arguments?.getString(CategoriesListFragment.ARG_CATEGORY_IMAGE_URL)

        binding.tvRecipesTitle.text = categoryName
        categoryImageUrl?.let { loadImageFromAssets(binding.ivCategoryImage, it) }

        val recipes = STUB.getRecipesByCategoryId(categoryId ?: 0)

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
        val recipe = STUB.getRecipeById(recipeId)

        if (recipe != null) {
            val bundle = Bundle().apply {
                putParcelable(RecipeFragment.ARG_RECIPE, recipe)
            }

            parentFragmentManager.commit {
                replace<RecipeFragment>(R.id.mainContainer, args = bundle)
                addToBackStack(null)
            }
        }
    }
}
