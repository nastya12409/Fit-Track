package com.example.fitnesstracker.data

// DTO — як виглядає ДОКУМЕНТ у колекції Firestore.
// Мапери (DTO → Entity → доменна модель) окремо — див. Mappers.kt
data class SectionDto(
    val id: Int = 0,
    val emoji: String = "",
    val title: String = "",
    val shortDescription: String = "",
    val fullDescription: String = "",
    val features: List<String> = emptyList(),
    val containerColor: String = "#E2E2E9"
)