package com.example.englishwordsapp.di

import androidx.lifecycle.SavedStateHandle

interface Factory<T> {
    fun create(savedStateHandle: SavedStateHandle): T
}