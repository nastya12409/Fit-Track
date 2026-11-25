package com.example.fitnesstracker.domain

import com.example.fitnesstracker.data.local.FoodEntry
import kotlinx.coroutines.flow.Flow

// Щоденник харчування: тільки локальні дані (Room), без мережі.
interface FoodRepository {
    // Записи певного дня; Flow сам оновить екран після змін
    fun observeByDate(date: String): Flow<List<FoodEntry>>

    // Додати запис
    suspend fun add(entry: FoodEntry)

    // Видалити запис за id
    suspend fun delete(id: Int)
}