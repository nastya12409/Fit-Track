package com.example.fitnesstracker.domain

import com.example.fitnesstracker.data.local.Article
import kotlinx.coroutines.flow.Flow

// Матеріали про спорт: тільки локальні дані (Room), без мережі.
// Тут одна єдина модель, бо джерело даних одне — мапер не потрібен
interface ArticleRepository {
    // Усі статті; Flow сам оновить екран після змін
    fun observeAll(): Flow<List<Article>>

    // Вставити вшиті статті, якщо таблиця порожня (перший запуск)
    suspend fun seedIfEmpty()

    // Додати статтю
    suspend fun add(article: Article)

    // Видалити статтю за id
    suspend fun delete(id: Int)
}