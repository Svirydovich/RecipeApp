package com.example.englishwordsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.englishwordsapp.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoriesAdapter: CategoriesListAdapter

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

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

        categoryId = arguments?.getInt("ARG_CATEGORY_ID")
        categoryName = arguments?.getString("ARG_CATEGORY_NAME")
        categoryImageUrl = arguments?.getString("ARG_CATEGORY_IMAGE_URL")

        categoriesAdapter = CategoriesListAdapter(emptyList())

        binding.rvRecipes.apply {
            adapter = categoriesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            categoriesAdapter.setOnItemClickListener(object :
                CategoriesListAdapter.OnItemClickListener {
                override fun onItemClick(categoryId: Int) {
                    openRecipesByCategoryId(categoryId)
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        parentFragmentManager.commit {
            replace(R.id.mainContainer, RecipesListFragment())
            addToBackStack(null)
        }
    }
}
