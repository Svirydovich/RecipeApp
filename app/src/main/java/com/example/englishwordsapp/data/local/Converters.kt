package com.example.englishwordsapp.data.local

import androidx.room.TypeConverter
import com.example.englishwordsapp.model.Ingredient
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromIngredientList(value: List<Ingredient>): String =
        Json.encodeToString(value)

    @TypeConverter
    fun toIngredientList(value: String): List<Ingredient> =
        Json.decodeFromString(value)
}
