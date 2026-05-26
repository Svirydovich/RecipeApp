package com.example.englishwordsapp.ui.fragments.categories

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.englishwordsapp.R
import com.example.englishwordsapp.databinding.FragmentListCategoriesBinding
import com.example.englishwordsapp.ui.adapters.CategoriesListAdapter
import com.example.englishwordsapp.ui.fragments.recipes.recipes_list.RecipesListFragment

class CategoriesListFragment : Fragment() {

    companion object {
        const val ARG_CATEGORY_ID = "ARG_CATEGORY_ID"
        const val ARG_CATEGORY_NAME = "ARG_CATEGORY_NAME"
        const val ARG_CATEGORY_IMAGE_URL = "ARG_CATEGORY_IMAGE_URL"
    }

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoriesListViewModel by viewModels()
    private lateinit var categoriesAdapter: CategoriesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        viewModel.state.observe(viewLifecycleOwner) { state -> updateUI(state) }
    }

    private fun setupRecyclerView() {
        categoriesAdapter = CategoriesListAdapter(emptyList())

        binding.rvCategories.adapter = categoriesAdapter

        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI(state: CategoriesListState) {
        categoriesAdapter.categories = state.categories
        categoriesAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val category = viewModel.getCategoryById(categoryId)

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
