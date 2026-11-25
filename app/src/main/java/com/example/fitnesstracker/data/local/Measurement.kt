package com.example.fitnesstracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Таблиця «Заміри»: один рядок = один вимір (звичайно — один за день)
@Entity(tableName = "measurements")
data class Measurement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,        // ключ дня, напр. «25.11.2026»
    val weightKg: Double,    // вага, кг
    val waistCm: Double,     // талія, см
    val hipsCm: Double       // стегна, см
)