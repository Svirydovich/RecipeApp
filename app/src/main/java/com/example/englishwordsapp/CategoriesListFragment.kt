package com.example.englishwordsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.englishwordsapp.databinding.FragmentListCategoriesBinding
import com.example.englishwordsapp.model.Category

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

        categoriesAdapter.setOnItemClickListener(object : CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(category: Category) {
                openRecipesByCategoryId(category)
            }
        })
    }

    private fun openRecipesByCategoryId(category: Category) {
        parentFragmentManager.commit {
            replace(R.id.mainContainer, RecipesListFragment())
            addToBackStack(null)
        }
    }
}
