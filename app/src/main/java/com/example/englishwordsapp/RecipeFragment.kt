package com.example.englishwordsapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.englishwordsapp.databinding.FragmentRecipeBinding
import com.example.englishwordsapp.model.Recipe
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {

    companion object {
        const val ARG_RECIPE = "arg_recipe"
    }

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

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

        val recipe =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
            } else {
                @Suppress("DEPRECATION")
                (arguments?.getParcelable(ARG_RECIPE))
            }

        if (recipe != null) {
            initUI(recipe)
            initRecycler(recipe)
        }
    }

    private fun initUI(recipe: Recipe) {
        binding.tvRecipeName.text = recipe.title
        loadImageFromAssets(binding.ivRecipeImage, recipe.imageUrl)
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
        binding.rvIngredients.adapter = IngredientsAdapter(recipe.ingredients)
        binding.rvIngredients.layoutManager = LinearLayoutManager(context)
        binding.rvIngredients.addItemDecoration(
            MaterialDividerItemDecoration(
                requireContext(),
                MaterialDividerItemDecoration.VERTICAL
            ).apply {
                isLastItemDecorated = false
                setDividerColorResource(requireContext(), R.color.white)
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
                setDividerColorResource(requireContext(), R.color.white)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
