package com.example.englishwordsapp.ui.fragments.recipes.recipes_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.englishwordsapp.R
import com.example.englishwordsapp.databinding.FragmentRecipesListBinding
import com.example.englishwordsapp.ui.adapters.RecipeAdapter
import com.example.englishwordsapp.ui.fragments.categories.CategoriesListFragment
import com.example.englishwordsapp.ui.fragments.recipes.recipe.RecipeFragment

class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipesListViewModel by viewModels()
    private lateinit var recipeAdapter: RecipeAdapter

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

        val categoryId = arguments?.getInt(CategoriesListFragment.ARG_CATEGORY_ID) ?: return
        val categoryName = arguments?.getString(CategoriesListFragment.ARG_CATEGORY_NAME) ?: ""
        val categoryImageUrl =
            arguments?.getString(CategoriesListFragment.ARG_CATEGORY_IMAGE_URL) ?: ""

        setupRecyclerView()

        viewModel.loadRecipesByCategory(categoryId, categoryName, categoryImageUrl)

        viewModel.state.observe(viewLifecycleOwner) { state -> updateUI(state) }
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter(emptyList(), { recipeId ->
            openRecipeByRecipeId(recipeId)
        }, requireContext())

        binding.rvRecipes.apply {
            adapter = recipeAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI(state: RecipesListState) {
        binding.tvRecipesTitle.text = state.categoryName
        binding.ivCategoryImage.setImageDrawable(state.categoryImage)

        recipeAdapter.recipes = state.recipes
        recipeAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = Bundle().apply {
            putInt(RecipeFragment.ARG_RECIPE_ID, recipeId)
        }

        findNavController().navigate(R.id.recipeFragment, bundle)
    }
}
