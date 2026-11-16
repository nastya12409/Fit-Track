package com.example.fitnesstracker.data

import com.example.fitnesstracker.MenuSection
import com.example.fitnesstracker.domain.SectionRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


// Реалізація Repository: дані приходять з Firestore (колекція "sections")
class SectionRepositoryImpl : SectionRepository {

    override suspend fun getSections(): List<MenuSection> = try {
        // Один виклик до Firestore; await() "чекає" відповіді в корутині
        val snapshot = FirebaseFirestore.getInstance()
            .collection(COLLECTION_SECTIONS)
            .get()
            .await()

        // Перетворюємо документи у DTO, а потім — у доменні моделі
        snapshot.toObjects(SectionDto::class.java)
            .map { it.toDomain() }
    } catch (e: Exception) {
        // Мережа недоступна / дані зламані — не падаємо, повертаємо порожнє
        emptyList()
    }

    companion object {
        private const val COLLECTION_SECTIONS = "sections"
    }
}