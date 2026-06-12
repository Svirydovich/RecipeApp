package com.example.englishwordsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.englishwordsapp.model.Category

@Database(entities = [Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
}
