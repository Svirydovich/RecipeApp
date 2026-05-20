package com.example.englishwordsapp.ui.fragments.recipes.recipe

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.englishwordsapp.R
import com.example.englishwordsapp.data.RecipesRepository
import com.example.englishwordsapp.databinding.FragmentRecipeBinding
import com.example.englishwordsapp.model.Recipe
import com.example.englishwordsapp.ui.adapters.IngredientsAdapter
import com.example.englishwordsapp.ui.adapters.MethodAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {

    companion object {
        const val ARG_RECIPE_ID = "arg_recipe"
    }

    private val viewModel: RecipeViewModel by viewModels()
    private var isFavorite = false
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: RecipesRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        repository = RecipesRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeId = arguments?.getInt(RecipeFragment.ARG_RECIPE_ID) ?: return

        viewModel.loadRecipe(recipeId)

        val recipe =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable(ARG_RECIPE_ID, Recipe::class.java)
            } else {
                @Suppress("DEPRECATION")
                (arguments?.getParcelable(ARG_RECIPE_ID))
            }

        if (recipe != null) {
            initUI(recipe)
            initRecycler(recipe)
        }

        viewModel.recipeState.observe(viewLifecycleOwner) { state ->
            Log.d("RecipeFragment", "isFavorites: ${state.isFavorites}")
            val iconRes = if (state.isFavorites) R.drawable.ic_heart else R.drawable.ic_heart_empty
            binding.favoriteButton.setImageResource(iconRes)
        }
    }

    private fun initUI(recipe: Recipe) {
        binding.tvRecipeName.text = recipe.title
        loadImageFromAssets(binding.ivRecipeImage, recipe.imageUrl)

        val favorites = repository.getFavorites()

        isFavorite = favorites.contains(recipe.id.toString())

        viewModel.initializeRecipe(recipe, isFavorite)

        binding.favoriteButton.setOnClickListener {
            viewModel.toggleFavorite()
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

    private fun initRecycler(recipe: Recipe) {
        val ingredientsAdapter = IngredientsAdapter(recipe.ingredients)
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvIngredients.layoutManager = LinearLayoutManager(context)
        binding.rvIngredients.addItemDecoration(
            MaterialDividerItemDecoration(
                requireContext(),
                MaterialDividerItemDecoration.VERTICAL
            ).apply {
                isLastItemDecorated = false
                setDividerColorResource(requireContext(), R.color.dark_white)
                dividerInsetStart = resources.getDimensionPixelSize(R.dimen.padding_item)
                dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.padding_item)
            }
        )

        binding.rvMethod.adapter = MethodAdapter(recipe.method)
        binding.rvMethod.layoutManager = LinearLayoutManager(context)
        binding.rvMethod.addItemDecoration(
            MaterialDividerItemDecoration(
                requireContext(),
                MaterialDividerItemDecoration.VERTICAL
            ).apply {
                isLastItemDecorated = false
                setDividerColorResource(requireContext(), R.color.dark_white)
                dividerInsetStart = resources.getDimensionPixelSize(R.dimen.padding_item)
                dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.padding_item)
            }
        )

        binding.sbServings.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvServingCount.text = progress.toString()
                ingredientsAdapter.updateIngredients(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        }
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
