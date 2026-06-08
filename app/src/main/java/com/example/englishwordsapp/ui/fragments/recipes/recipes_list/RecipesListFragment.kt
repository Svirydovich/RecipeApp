package com.example.englishwordsapp.ui.fragments.recipes.recipes_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.englishwordsapp.R
import com.example.englishwordsapp.databinding.FragmentRecipesListBinding
import com.example.englishwordsapp.ui.adapters.RecipeAdapter

class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipesListViewModel by viewModels()
    private lateinit var recipeAdapter: RecipeAdapter
    private val args: RecipesListFragmentArgs by navArgs()

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

        val category = args.category

        setupRecyclerView()

        viewModel.loadRecipesByCategory(category.id, category.title, category.imageUrl)

        viewModel.state.observe(viewLifecycleOwner) { state -> updateUI(state) }
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter(emptyList()) { recipeId -> openRecipeByRecipeId(recipeId) }

        binding.rvRecipes.apply {
            adapter = recipeAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI(state: RecipesListState) {
        binding.tvRecipesTitle.text = state.categoryName
        Glide.with(binding.ivCategoryImage.context)
            .load(state.categoryImageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(binding.ivCategoryImage)

        recipeAdapter.recipes = state.recipes
        recipeAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        findNavController().navigate(
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeId)
        )
    }
}
