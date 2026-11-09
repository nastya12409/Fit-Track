package com.example.fitnesstracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.data.SectionRepositoryImpl
import com.example.fitnesstracker.domain.GetSectionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Стан екрана: всі дані, які потрібні для малювання першого екрана
data class ListUiState(
    val sections: List<MenuSection> = emptyList(), // список розділів
    val searchQuery: String = "", // текст пошуку
    val isLoading: Boolean = true // прапорець завантаження
)

// Клас ViewModel: «мозг» екрана
class HomeViewModel(
    private val getSectionsUseCase: GetSectionsUseCase = GetSectionsUseCase(
        SectionRepositoryImpl()
    )
) : ViewModel() {
    // Приватний стан
    private val _uiState = MutableStateFlow(ListUiState())
    // Публічний read-only
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    init {
        loadSections()
    }
    private fun loadSections() {
        viewModelScope.launch {
            val sections = getSectionsUseCase()
            _uiState.value = ListUiState(
                sections = sections,
                isLoading = false
            )
        }
    }
    // Дії користувача — методи ViewModel
    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }
}