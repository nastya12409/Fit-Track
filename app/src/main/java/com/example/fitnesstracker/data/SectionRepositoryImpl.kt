package com.example.fitnesstracker.data

import androidx.compose.ui.graphics.Color
import com.example.fitnesstracker.MenuSection
import com.example.fitnesstracker.domain.SectionRepository

// Реалізація контракту: тестовий список переїхав сюди з ViewModel
class SectionRepositoryImpl : SectionRepository {

    override suspend fun getSections(): List<MenuSection> = listOf(
        MenuSection(
            id = 1,
            emoji = "🍽",
            title = "Щоденник харчування",
            shortDescription = "Запис калорій, води та цукру",
            fullDescription = "Веди щоденний облік їжі: записуй продукти, калорійність та БЖВ, стеж за вживанням води та цукру.",
            features = listOf("Облік калорій та БЖВ", "Споживання води", "Облік цукру", "Підрахунок за день"),
            containerColor = Color(0xFF5E73A9)
        ),
        MenuSection(
            id = 2,
            emoji = "📝",
            title = "Замітки",
            shortDescription = "Нотатки про харчування та тренування",
            fullDescription = "Зберігай свої думки: нотатки про тренування, харчування, самопочуття та цілі.",
            features = listOf("Нотатки про харчування", "Нотатки про тренування", "Історія записів"),
            containerColor = Color(0xFF6E7488)
        ),
        MenuSection(
            id = 3,
            emoji = "🏃",
            title = "Матеріали про спорт",
            shortDescription = "Статті та поради з тренувань",
            fullDescription = "Корисні статті про тренування, харчування та відновлення — щоб тренуватися розумно і без травм.",
            features = listOf("Статті про вправи", "Поради з харчування", "Відновлення та розтяжка"),
            containerColor = Color(0xFF8A6A89)
        ),
        MenuSection(
            id = 4,
            emoji = "📒",
            title = "Трекер звичок",
            shortDescription = "Контроль своїх звичок",
            fullDescription = "Відстежуй щоденні звички: серії виконання (streak), нагадування та історія за кожен день.",
            features = listOf("Список звичок на сьогодні", "Серія виконання (streak)", "Нагадування", "Історія виконання"),
            containerColor = Color(0xFF6E7488)
        ),
        MenuSection(
            id = 5,
            emoji = "⚙️",
            title = "Налаштування",
            shortDescription = "Профіль, тема та інші параметри",
            fullDescription = "Налаштуй застосунок під себе: дані профілю, цілі, тема оформлення та інші параметри.",
            features = listOf("Профіль користувача", "Цілі та параметри", "Тема оформлення"),
            containerColor = Color(0xFFE2E2E9)
        )
    )
}