package com.example.fitnesstracker.domain

import com.example.fitnesstracker.MenuSection
import kotlinx.coroutines.flow.Flow

// ЩО можна отримати, але не ЯК вони зберігаються
interface SectionRepository {
    // Завжди читаємо з локальної бази (єдине джерело істини)
    fun observeItems(): Flow<List<MenuSection>>

    // Оновлення з мережі: true — успіх, false — помилка (кеш НЕ стирається)
    suspend fun refresh(): Boolean
}