package com.example.englishwordsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.englishwordsapp.model.Category
import com.example.englishwordsapp.model.Recipe
import androidx.room.TypeConverters

@Database(entities = [Category::class, Recipe::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
    abstract fun recipesDao(): RecipesDao
}
