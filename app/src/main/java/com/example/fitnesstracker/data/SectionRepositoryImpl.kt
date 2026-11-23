package com.example.fitnesstracker.data

import com.example.fitnesstracker.MenuSection
import com.example.fitnesstracker.data.local.SectionDao
import com.example.fitnesstracker.domain.SectionRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

// offline-first: читаємо завжди з Room, оновлюємо — з Firestore
// Залежності (dao, firestore) надає Koin через конструктор
class SectionRepositoryImpl(
    private val dao: SectionDao,
    private val firestore: FirebaseFirestore
) : SectionRepository {

    // Дані йдуть завжди з локальної бази; Flow сам оновить екран після кешування
    override fun observeItems(): Flow<List<MenuSection>> =
        dao.getAll().map { entities -> entities.map { it.toDomain() } }

    // Мережа → Room; за помилки кеш залишається як є
    override suspend fun refresh(): Boolean = try {
        val snapshot = firestore
            .collection(COLLECTION_SECTIONS)
            .get(Source.SERVER)   // тільки сервер: офлайн = помилка, а не кеш Firestore
            .await()

        // DTO → Entity → запис у кеш
        val entities = snapshot.toObjects(SectionDto::class.java)
            .map { it.toEntity() }
        dao.insertAll(entities)

        true
    } catch (e: Exception) {
        false // не очищуємо кеш — показуємо те, що вже є
    }

    companion object {
        private const val COLLECTION_SECTIONS = "sections"
    }
}