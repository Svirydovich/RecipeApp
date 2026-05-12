package com.example.englishwordsapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.englishwordsapp.databinding.ItemIngredientBinding
import com.example.englishwordsapp.model.Ingredient

class IngredientsAdapter(private val ingredients: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>() {

    private var quantity = 1

    class IngredientViewHolder(private val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(ingredient: Ingredient, quantity: Int) {
            binding.tvIngredientName.text = ingredient.description

            val baseQuantity = ingredient.quantity.toDoubleOrNull() ?: 0.0

            val totalQuantity = baseQuantity * quantity

            val quantityText = if (totalQuantity == 0.0) {
                ingredient.quantity
            } else if (totalQuantity % 1 == 0.0) {
                totalQuantity.toInt().toString()
            } else String.format("%.1f", totalQuantity)

            binding.tvIngredientQuantity.text = "$quantityText ${ingredient.unitOfMeasure}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val binding =
            ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(ingredients[position], quantity)
    }

    override fun getItemCount(): Int = ingredients.size

    fun updateIngredients(newQuantity: Int) {
        quantity = newQuantity
        notifyDataSetChanged()
    }
}
