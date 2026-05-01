package com.example.englishwordsapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.englishwordsapp.databinding.ItemCategoryBinding
import com.example.englishwordsapp.model.Category

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(category: Category)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.tvCategoryName.text = category.title
            binding.tvCategoryDescription.text = category.description

            try {
                val drawable = Drawable.createFromStream(
                    itemView.context.assets.open(category.imageUrl),
                    null
                )
                binding.ivCategoryImage.setImageDrawable(drawable)
            } catch (e: Exception) {
                Log.e("CategoriesListAdapter", "Error loading image", e)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
        viewHolder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size
}
