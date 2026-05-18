package com.example.englishwordsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.englishwordsapp.databinding.FragmentListCategoriesBinding
import androidx.fragment.app.replace
import com.example.englishwordsapp.R
import com.example.englishwordsapp.data.RecipesRepository
import com.example.englishwordsapp.ui.adapters.CategoriesListAdapter

class CategoriesListFragment : Fragment() {

    companion object {
        const val ARG_CATEGORY_ID = "ARG_CATEGORY_ID"
        const val ARG_CATEGORY_NAME = "ARG_CATEGORY_NAME"
        const val ARG_CATEGORY_IMAGE_URL = "ARG_CATEGORY_IMAGE_URL"
    }

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: RecipesRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        repository = RecipesRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        val categories = repository.getCategories()
        val categoriesAdapter = CategoriesListAdapter(categories)

        binding.rvCategories.adapter = categoriesAdapter

        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val category = repository.getCategories().find { it.id == categoryId }

        if (category != null) {
            val bundle = Bundle().apply {
                putInt(ARG_CATEGORY_ID, categoryId)
                putString(ARG_CATEGORY_NAME, category.title)
                putString(ARG_CATEGORY_IMAGE_URL, category.imageUrl)
            }

            parentFragmentManager.commit {
                replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
                addToBackStack(null)
            }
        }
    }
}
