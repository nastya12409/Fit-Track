package com.example.fitnesstracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.data.local.FoodEntry
import com.example.fitnesstracker.domain.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Сьогоднішня дата у форматі «25.11.2026» — ключ дня для записів
private fun today(): String =
    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())

// Стан екрана «Щоденник харчування»
data class FoodUiState(
    val date: String = today(),
    val entries: List<FoodEntry> = emptyList(),
    // Поля форми (рядки — бо це те, що ввів користувач)
    val name: String = "",
    val calories: String = "",
    val proteins: String = "",
    val fats: String = "",
    val carbs: String = "",
    val water: String = "",
    val sugar: String = "",
    // id рядка помилки з strings.xml або null
    val errorRes: Int? = null
) {
    // Ітого за день — рахується автоматично після кожної зміни списку
    val totalCalories: Int get() = entries.sumOf { it.calories }
    val totalProteins: Int get() = entries.sumOf { it.proteins }
    val totalFats: Int get() = entries.sumOf { it.fats }
    val totalCarbs: Int get() = entries.sumOf { it.carbs }
    val totalWater: Int get() = entries.sumOf { it.water }
    val totalSugar: Int get() = entries.sumOf { it.sugar }
}

//  форма + запис у Room + підписка на записи дня
class FoodViewModel(
    private val repository: FoodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FoodUiState())
    val uiState: StateFlow<FoodUiState> = _uiState.asStateFlow()

    init {
        // Підписка на записи сьогоднішнього дня:
        // додавання/видалення в Room самотужки оновить екран
        viewModelScope.launch {
            repository.observeByDate(_uiState.value.date).collect { entries ->
                _uiState.value = _uiState.value.copy(entries = entries)
            }
        }
    }

    // Оновлення полів форми (помилка зникає при наборі)
    fun onNameChange(v: String) {
        _uiState.value = _uiState.value.copy(name = v, errorRes = null)
    }
    fun onCaloriesChange(v: String) {
        _uiState.value = _uiState.value.copy(calories = v, errorRes = null)
    }
    fun onProteinsChange(v: String) {
        _uiState.value = _uiState.value.copy(proteins = v, errorRes = null)
    }
    fun onFatsChange(v: String) {
        _uiState.value = _uiState.value.copy(fats = v, errorRes = null)
    }
    fun onCarbsChange(v: String) {
        _uiState.value = _uiState.value.copy(carbs = v, errorRes = null)
    }
    fun onWaterChange(v: String) {
        _uiState.value = _uiState.value.copy(water = v, errorRes = null)
    }
    fun onSugarChange(v: String) {
        _uiState.value = _uiState.value.copy(sugar = v, errorRes = null)
    }

    // Додавання запису з валідацією
    fun addEntry() {
        val s = _uiState.value
        if (s.name.isBlank()) {
            _uiState.value = s.copy(errorRes = R.string.food_error_name)
            return
        }
        val calories = s.calories.toIntOrNull()
        if (calories == null) {
            _uiState.value = s.copy(errorRes = R.string.food_error_calories)
            return
        }
        viewModelScope.launch {
            repository.add(
                FoodEntry(
                    date = s.date,
                    name = s.name.trim(),
                    calories = calories,
                    proteins = s.proteins.toIntOrNull() ?: 0,
                    fats = s.fats.toIntOrNull() ?: 0,
                    carbs = s.carbs.toIntOrNull() ?: 0,
                    water = s.water.toIntOrNull() ?: 0,
                    sugar = s.sugar.toIntOrNull() ?: 0
                )
            )
            // Очищення форми після успішного додавання
            _uiState.value = _uiState.value.copy(
                name = "", calories = "", proteins = "", fats = "",
                carbs = "", water = "", sugar = "", errorRes = null
            )
        }
    }

    // Видалення запису зі списку
    fun deleteEntry(id: Int) {
        viewModelScope.launch { repository.delete(id) }
    }
}