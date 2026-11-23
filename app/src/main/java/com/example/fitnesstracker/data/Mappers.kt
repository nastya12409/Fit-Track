package com.example.fitnesstracker.data

import androidx.compose.ui.graphics.Color
import com.example.fitnesstracker.MenuSection
import com.example.fitnesstracker.data.local.SectionEntity

// Мапер 1: DTO (Firestore) → Entity (Room)
fun SectionDto.toEntity(): SectionEntity = SectionEntity(
    id = id,
    emoji = emoji,
    title = title,
    shortDescription = shortDescription,
    fullDescription = fullDescription,
    features = features,
    containerColor = containerColor
)

// Мапер 2: Entity (Room) → доменна модель
fun SectionEntity.toDomain(): MenuSection = MenuSection(
    id = id,
    emoji = emoji,
    title = title,
    shortDescription = shortDescription,
    fullDescription = fullDescription,
    features = features,
    containerColor = Color(android.graphics.Color.parseColor(containerColor))
)