package com.example.englishwordsapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.englishwordsapp.R
import com.example.englishwordsapp.databinding.ItemRecipeBinding
import com.example.englishwordsapp.di.AppModule.Companion.BASE_URL
import com.example.englishwordsapp.di.AppModule.Companion.IMAGES_PATH
import com.example.englishwordsapp.model.Recipe

class RecipeAdapter(
    var recipes: List<Recipe>,
    private val onRecipeClick: (recipeId: Int) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {
            binding.tvRecipeTitle.text = recipe.title

            val imageUrl = "$BASE_URL$IMAGES_PATH${recipe.imageUrl}"
            Glide.with(binding.ivRecipeImage.context)
                .load(imageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.ivRecipeImage)

            binding.root.setOnClickListener { onRecipeClick(recipe.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int = recipes.size
}
