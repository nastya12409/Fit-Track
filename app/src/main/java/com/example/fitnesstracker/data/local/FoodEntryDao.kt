package com.example.fitnesstracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodEntryDao {
    // Записи обраного дня; Flow сам оновить екран після змін
    @Query("SELECT * FROM food_entries WHERE date = :date ORDER BY id")
    fun getByDate(date: String): Flow<List<FoodEntry>>

    // Новий запис (id згенерує Room)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: FoodEntry)

    // Видалення запису за id
    @Query("DELETE FROM food_entries WHERE id = :id")
    suspend fun delete(id: Int)
}