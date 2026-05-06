package com.example.englishwordsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.englishwordsapp.databinding.FragmentRecipeBinding
import com.example.englishwordsapp.model.Recipe

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

        val recipe = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {
            @Suppress("DEPRECATION")
            (arguments?.getParcelable(ARG_RECIPE))
        }

        if (recipe != null) {
            binding.tvRecipeIdentifier.text = recipe.title
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
