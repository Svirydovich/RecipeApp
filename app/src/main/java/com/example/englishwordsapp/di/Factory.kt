package com.example.englishwordsapp.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

interface Factory<T> : ViewModelProvider.Factory {
    fun create(savedStateHandle: SavedStateHandle): T

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return create(SavedStateHandle()) as T
    }
}
