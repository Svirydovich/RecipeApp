package com.example.englishwordsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.englishwordsapp.databinding.FragmentRecipesListBinding
import com.example.englishwordsapp.model.Category

class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoriesAdapter: CategoriesListAdapter

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

        categoriesAdapter = CategoriesListAdapter(emptyList())

        binding.rvRecipes.apply {
            adapter = categoriesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            categoriesAdapter.setOnItemClickListener(object : CategoriesListAdapter.OnItemClickListener {
                override fun onItemClick(category: Category) {
                    openRecipesByCategoryId(category)
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openRecipesByCategoryId(category: Category) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.mainContainer, RecipesListFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
