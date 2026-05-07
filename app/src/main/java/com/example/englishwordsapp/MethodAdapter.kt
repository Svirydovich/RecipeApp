package com.example.englishwordsapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.englishwordsapp.databinding.ItemMethodBinding

class MethodAdapter(private val steps: List<String>) :
    RecyclerView.Adapter<MethodAdapter.MethodViewHolder>() {

    class MethodViewHolder(private val binding: ItemMethodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(step: String, position: Int) {
            binding.tvMethodStep.text = "${position + 1}. $step"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MethodViewHolder {
        val binding = ItemMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MethodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MethodViewHolder, position: Int) {
        holder.bind(steps[position], position)
    }

    override fun getItemCount(): Int = steps.size
}
