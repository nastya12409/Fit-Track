package com.example.fitnesstracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Таблиця "sections" у локальній базі Room
@Entity(tableName = "sections")
data class SectionEntity(
    @PrimaryKey val id: Int,
    val emoji: String,
    val title: String,
    val shortDescription: String,
    val fullDescription: String,
    val features: List<String>,      // конвертується у рядок
    val containerColor: String       // "#RRGGBB"
)