package com.example.fitnesstracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.domain.SectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Стан екрана: всі дані, які потрібні для малювання першого екрана
data class ListUiState(
    val sections: List<MenuSection> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val syncError: Boolean = false   // true = не вдалося оновити з мережі
)

// Клас ViewModel: «мозг» екрана
class HomeViewModel(
    private val repository: SectionRepository   // отримуємо з Koin, без налаштувань за замовчуванням
) : ViewModel() {

    // Приватний стан
    private val _uiState = MutableStateFlow(ListUiState())
    // Публічний read-only
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    init {
        // 1) Підписка на Room: дані з'являться навіть офлайн, з кешу
        viewModelScope.launch {
            repository.observeItems().collect { items ->
                _uiState.value = _uiState.value.copy(sections = items)
            }
        }
        // 2) Оновлення з мережі при вході на екран
        viewModelScope.launch {
            val success = repository.refresh()
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                syncError = !success
            )
        }
    }

    // Дії користувача — методи ViewModel
    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }
}