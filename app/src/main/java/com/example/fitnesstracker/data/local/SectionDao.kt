package com.example.fitnesstracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SectionDao {
    // Список для екрана: Flow сам повідомить про зміни після кешування
    @Query("SELECT * FROM sections ORDER BY id")
    fun getAll(): Flow<List<SectionEntity>>

    // Запис у кеш; REPLACE перезапише документи з тим самим id
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<SectionEntity>)

    // За потреби: один розділ за id
    @Query("SELECT * FROM sections WHERE id = :id")
    suspend fun getById(id: Int): SectionEntity?
}