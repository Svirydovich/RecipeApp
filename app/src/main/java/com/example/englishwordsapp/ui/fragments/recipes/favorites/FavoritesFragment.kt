package com.example.englishwordsapp.ui.fragments.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.englishwordsapp.R
import com.example.englishwordsapp.data.RecipesRepository
import com.example.englishwordsapp.databinding.FragmentFavoritesBinding
import com.example.englishwordsapp.ui.adapters.RecipeAdapter
import com.example.englishwordsapp.ui.fragments.recipes.recipe.RecipeFragment

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: RecipesRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        repository = RecipesRepository(requireContext())
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = repository.getRecipeById(recipeId)

        if (recipe != null) {
            val bundle = Bundle().apply {
                putParcelable(RecipeFragment.ARG_RECIPE_ID, recipe)
            }

            parentFragmentManager.commit {
                replace<RecipeFragment>(R.id.mainContainer, args = bundle)
                addToBackStack(null)
            }
        }
    }

    private fun initRecycler() {
        val favoriteIds = repository.getFavorites()

        val favoriteRecipes = repository.getRecipesByIds(favoriteIds)

        val recipeAdapter = RecipeAdapter(favoriteRecipes, { recipeId ->
            openRecipeByRecipeId(recipeId)
        }, requireContext())

        binding.rvFavorites.apply {
            adapter = recipeAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        if (favoriteRecipes.isEmpty()) {
            binding.rvFavorites.visibility = View.GONE
            binding.tvEmptyState.visibility = View.VISIBLE
        } else {
            binding.rvFavorites.visibility = View.VISIBLE
            binding.tvEmptyState.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
    }

}