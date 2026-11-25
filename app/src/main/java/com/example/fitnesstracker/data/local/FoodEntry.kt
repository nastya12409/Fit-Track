package com.example.fitnesstracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Таблиця «Щоденник харчування»: один рядок = один запис за день
@Entity(tableName = "food_entries")
data class FoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,      // ключ дня, напр. «25.11.2026»
    val name: String,      // що з'їли
    val calories: Int,     // ккал
    val proteins: Int,     // білки, г
    val fats: Int,         // жири, г
    val carbs: Int,        // вуглеводи, г
    val water: Int,        // випита вода, мл
    val sugar: Int         // цукр, г
)