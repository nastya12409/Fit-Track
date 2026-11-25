package com.example.fitnesstracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasurementDao {
    // Усі записи, новіші першими; Flow сам оновить екран після змін
    @Query("SELECT * FROM measurements ORDER BY id DESC")
    fun getAll(): Flow<List<Measurement>>

    // Новий запис (id згенерує Room)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: Measurement)

    // Видалення запису за id
    @Query("DELETE FROM measurements WHERE id = :id")
    suspend fun delete(id: Int)
}