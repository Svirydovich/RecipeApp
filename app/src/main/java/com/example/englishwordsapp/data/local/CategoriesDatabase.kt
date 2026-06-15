package com.example.englishwordsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.englishwordsapp.model.Category
import com.example.englishwordsapp.model.Recipe

@Database(entities = [Category::class, Recipe::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
    abstract fun recipesDao(): RecipesDao
}
