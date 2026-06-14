package com.example.englishwordsapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.englishwordsapp.model.Category

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM categories")
    fun getAll(): LiveData<List<Category>>

    @Query("SELECT * FROM categories")
    suspend fun getAllOnce(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<Category>)
}
