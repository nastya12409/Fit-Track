package com.example.fitnesstracker.data

import com.example.fitnesstracker.data.local.FoodEntry
import com.example.fitnesstracker.data.local.FoodEntryDao
import com.example.fitnesstracker.domain.FoodRepository
import kotlinx.coroutines.flow.Flow

// Реалізація працює тільки з Room: записи їжі нікуди не синхронізуємо
class FoodRepositoryImpl(
    private val dao: FoodEntryDao
) : FoodRepository {

    override fun observeByDate(date: String): Flow<List<FoodEntry>> =
        dao.getByDate(date)

    override suspend fun add(entry: FoodEntry) =
        dao.insert(entry)

    override suspend fun delete(id: Int) =
        dao.delete(id)
}