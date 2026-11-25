package com.example.fitnesstracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    // Усі статті; Flow сам оновить екран після змін
    @Query("SELECT * FROM articles ORDER BY id DESC")
    fun getAll(): Flow<List<Article>>

    // Кількість статей — щоб засіяти базу лише при першому запуску
    @Query("SELECT COUNT(*) FROM articles")
    suspend fun count(): Int

    // Вшиті статті (ігноруємо конфлікти — не перезаписуємо наявні)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(articles: List<Article>)

    // Нова стаття користувача (id згенерує Room)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    // Видалення статті за id
    @Query("DELETE FROM articles WHERE id = :id")
    suspend fun delete(id: Int)
}