package com.example.fitnesstracker.data

import com.example.fitnesstracker.data.local.Measurement
import com.example.fitnesstracker.data.local.MeasurementDao
import com.example.fitnesstracker.domain.MeasurementRepository
import kotlinx.coroutines.flow.Flow

// Реалізація працює тільки з Room: заміри нікуди не синхронізуємо
class MeasurementRepositoryImpl(
    private val dao: MeasurementDao
) : MeasurementRepository {

    override fun observeAll(): Flow<List<Measurement>> = dao.getAll()

    override suspend fun add(entry: Measurement) = dao.insert(entry)

    override suspend fun delete(id: Int) = dao.delete(id)
}