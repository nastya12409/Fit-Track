package com.example.fitnesstracker.domain

import com.example.fitnesstracker.data.local.Measurement
import kotlinx.coroutines.flow.Flow

// Заміри: тільки локальні дані (Room), без мережі.
// Віддаємо весь список — бо запис порівнюємо з попереднім (прогрес)
interface MeasurementRepository {
    // Усі записи (новіші першими); Flow сам оновить екран після змін
    fun observeAll(): Flow<List<Measurement>>

    // Додати запис
    suspend fun add(entry: Measurement)

    // Видалити запис за id
    suspend fun delete(id: Int)
}