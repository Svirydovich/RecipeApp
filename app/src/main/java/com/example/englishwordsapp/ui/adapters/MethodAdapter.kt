package com.example.englishwordsapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.englishwordsapp.databinding.ItemMethodBinding

class MethodAdapter : RecyclerView.Adapter<MethodAdapter.MethodViewHolder>() {
    var steps: List<String> = emptyList()

    class MethodViewHolder(private val binding: ItemMethodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(step: String) {
            binding.tvMethodStep.text = step
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MethodViewHolder {
        val binding = ItemMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MethodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MethodViewHolder, position: Int) {
        holder.bind(steps[position])
    }

    override fun getItemCount(): Int = steps.size
}
