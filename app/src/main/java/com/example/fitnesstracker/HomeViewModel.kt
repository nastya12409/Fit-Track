package com.example.fitnesstracker

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Стан екрана: всі дані, які потрібні для малювання першого екрана
data class ListUiState(
    val sections: List<MenuSection> = emptyList(), // список розділів
    val searchQuery: String = "", // текст пошуку
    val isLoading: Boolean = true // прапорець завантаження
)

// Клас ViewModel: «мозг» екрана, що переживає повороти та переходи
class HomeViewModel : ViewModel() {
    // Приватний стан — змінювати може цей клас
    private val _uiState = MutableStateFlow(ListUiState())
    // Публічний read-only — екран лише читає через collectAsStateWithLifecycle()
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()
    init {
        loadSections()
    }
    // Тестовий список
    private fun loadSections() {
        val testSections = listOf(
            MenuSection(
                id = 1,
                emoji = "🍽",
                title = "Щоденник харчування",
                shortDescription = "Запис калорій, води та цукру",
                fullDescription = "Веди щоденний облік їжі: записуй продукти, калорійність та БЖВ, стеж за вживанням води та цукру.",
                features = listOf("Облік калорій та БЖВ", "Споживання води", "Облік цукру", "Підрахунок за день")
            ),
            MenuSection(
                id = 2,
                emoji = "📝",
                title = "Замітки",
                shortDescription = "Нотатки про харчування та тренування",
                fullDescription = "Зберігай свої думки: нотатки про тренування, харчування, самопочуття та цілі.",
                features = listOf("Нотатки про харчування", "Нотатки про тренування", "Історія записів")
            ),
            MenuSection(
                id = 3,
                emoji = "🏃",
                title = "Матеріали про спорт",
                shortDescription = "Статті та поради з тренувань",
                fullDescription = "Корисні статті про тренування, харчування та відновлення — щоб тренуватися розумно і без травм.",
                features = listOf("Статті про вправи", "Поради з харчування", "Відновлення та розтяжка")
            ),
            MenuSection(
                id = 4,
                emoji = "📒",
                title = "Трекер звичок",
                shortDescription = "Контроль своїх звичок",
                fullDescription = "Відстежуй щоденні звички: серії виконання (streak), нагадування та історія за кожен день.",
                features = listOf("Список звичок на сьогодні", "Серія виконання (streak)", "Нагадування", "Історія виконання")
            ),
            MenuSection(
                id = 5,
                emoji = "⚙️",
                title = "Налаштування",
                shortDescription = "Профіль, тема та інші параметри",
                fullDescription = "Налаштуй застосунок під себе: дані профілю, цілі, тема оформлення та інші параметри.",
                features = listOf("Профіль користувача", "Цілі та параметри", "Тема оформлення")
            )
        )
        _uiState.value = ListUiState(
            sections = testSections,
            isLoading = false
        )
    }
    // Дії користувача — методи ViewModel
    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }
}