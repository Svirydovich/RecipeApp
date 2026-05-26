package com.example.englishwordsapp.ui.fragments.recipes.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.englishwordsapp.R
import com.example.englishwordsapp.databinding.FragmentFavoritesBinding
import com.example.englishwordsapp.ui.adapters.RecipeAdapter
import com.example.englishwordsapp.ui.fragments.recipes.recipe.RecipeFragment

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.state.observe(viewLifecycleOwner) { state -> updateUI(state) }
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter(emptyList(), { recipeId ->
            openRecipeByRecipeId(recipeId)
        }, requireContext())

        binding.rvFavorites.apply {
            adapter = recipeAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI(state: FavoritesState) {
        recipeAdapter.recipes = state.recipes
        recipeAdapter.notifyDataSetChanged()

        if (state.isEmpty) {
            binding.rvFavorites.visibility = View.GONE
            binding.tvEmptyState.visibility = View.VISIBLE
        } else {
            binding.rvFavorites.visibility = View.VISIBLE
            binding.tvEmptyState.visibility = View.GONE
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
