package com.example.fitnesstracker.data

import androidx.compose.ui.graphics.Color
import com.example.fitnesstracker.MenuSection
data class SectionDto(
    val id: Int = 0,
    val emoji: String = "",
    val title: String = "",
    val shortDescription: String = "",
    val fullDescription: String = "",
    val features: List<String> = emptyList(),
    val containerColor: String = "#E2E2E9"  //колір у базі зберігається рядком
) {
    //конвертуємо рядок кольору в Col
    fun toDomain(): MenuSection = MenuSection(
        id = id,
        emoji = emoji,
        title = title,
        shortDescription = shortDescription,
        fullDescription = fullDescription,
        features = features,
        containerColor = Color(android.graphics.Color.parseColor(containerColor))
    )
}