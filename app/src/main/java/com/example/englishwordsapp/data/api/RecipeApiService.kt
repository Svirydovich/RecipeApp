package com.example.englishwordsapp.data.api

import com.example.englishwordsapp.model.Category
import com.example.englishwordsapp.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApiService {

    @GET("recipe/{id}")
    fun getRecipeById(@Path("id") id: Int): Call<Recipe>

    @GET("recipes")
    fun getRecipes(): Call<List<Recipe>>

    @GET("category/{id}")
    fun getCategoryById(@Path("id") id: Int): Call<Category>

    @GET("category/{id}/recipes")
    fun getRecipesByCategory(@Path("id") id: Int): Call<List<Recipe>>

    @GET("category")
    fun getCategories(): Call<List<Category>>
}
