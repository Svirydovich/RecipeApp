package com.example.englishwordsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.englishwordsapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding get() = _binding!!

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
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        val categories = STUB.getCategories()
        val categoriesAdapter = CategoriesListAdapter(categories)

        binding.rvCategories.adapter = categoriesAdapter
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())

        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val category = STUB.getCategories().find { it.id == categoryId }

        val categoryName = category?.title ?: ""
        val categoryImageUrl = category?.imageUrl ?: ""

        val bundle = Bundle().apply {
            putInt("ARG_CATEGORY_ID", categoryId)
            putString("ARG_CATEGORY_NAME", categoryName)
            putString("ARG_CATEGORY_IMAGE_URL", categoryImageUrl)
        }

        parentFragmentManager.commit {
            replace(R.id.mainContainer, RecipesListFragment::class.java, bundle)
            addToBackStack(null)
        }
    }
}
