package com.example.englishwordsapp.ui.fragments.recipes.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.englishwordsapp.R
import com.example.englishwordsapp.databinding.FragmentRecipeBinding
import com.example.englishwordsapp.di.RecipeApplication
import com.example.englishwordsapp.di.RecipeViewModelFactory
import com.example.englishwordsapp.ui.adapters.IngredientsAdapter
import com.example.englishwordsapp.ui.adapters.MethodAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var methodAdapter: MethodAdapter
    private val viewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory(
            (requireActivity().application as RecipeApplication).appContainer.recipesRepository
        )
    }
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private val args: RecipeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeId = args.recipeId

        viewModel.loadRecipe(recipeId)

        setupAdapters()

        binding.sbServings.setOnSeekBarChangeListener(
            PortionSeekBarListener { progress ->
                viewModel.updatePortions(progress + 1)
            }
        )

        viewModel.recipeState.observe(viewLifecycleOwner) { state ->
            Log.d("RecipeFragment", "isFavorites: ${state.isFavorites}")
            val iconRes = if (state.isFavorites) R.drawable.ic_heart else R.drawable.ic_heart_empty
            binding.favoriteButton.setImageResource(iconRes)

            Glide.with(binding.ivRecipeImage.context)
                .load(state.recipeImageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.ivRecipeImage)

            state.recipe?.let {
                updateUI(state)
            }
        }

        binding.favoriteButton.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }

    private fun setupAdapters() {
        ingredientsAdapter = IngredientsAdapter()
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvIngredients.layoutManager = LinearLayoutManager(context)
        binding.rvIngredients.addItemDecoration(
            MaterialDividerItemDecoration(requireContext(), MaterialDividerItemDecoration.VERTICAL)
                .apply {
                    isLastItemDecorated = false
                    setDividerColorResource(requireContext(), R.color.dark_white)
                    dividerInsetStart = resources.getDimensionPixelSize(R.dimen.padding_item)
                    dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.padding_item)
                }
        )

        methodAdapter = MethodAdapter()
        binding.rvMethod.adapter = methodAdapter
        binding.rvMethod.layoutManager = LinearLayoutManager(context)
        binding.rvMethod.addItemDecoration(
            MaterialDividerItemDecoration(requireContext(), MaterialDividerItemDecoration.VERTICAL)
                .apply {
                    isLastItemDecorated = false
                    setDividerColorResource(requireContext(), R.color.dark_white)
                    dividerInsetStart = resources.getDimensionPixelSize(R.dimen.padding_item)
                    dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.padding_item)
                }
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI(recipeState: RecipeState) {
        binding.tvRecipeName.text = recipeState.recipe?.title
        binding.tvServingCount.text = recipeState.servings.toString()

        ingredientsAdapter.ingredients = recipeState.recipe?.ingredients ?: emptyList()
        ingredientsAdapter.updateIngredients(recipeState.servings)

        methodAdapter.steps = recipeState.recipe?.method ?: emptyList()
        methodAdapter.notifyDataSetChanged()

        binding.sbServings.progress = recipeState.servings - 1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class PortionSeekBarListener(
        val onChangePortions: (Int) -> Unit
    ) : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) onChangePortions(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }
}
