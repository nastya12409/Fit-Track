package com.example.fitnesstracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.data.local.Measurement
import com.example.fitnesstracker.domain.MeasurementRepository
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

// Стан екрана «Заміри»
data class MeasurementsUiState(
    val date: String = today(),
    val entries: List<Measurement> = emptyList(), // новіші першими
    // Поля форми (рядки — бо це те, що ввів користувач)
    val weight: String = "",
    val waist: String = "",
    val hips: String = "",
    // id рядка помилки з strings.xml або null
    val errorRes: Int? = null
) {
    // Приріст/спад проти попереднього запису;
    // null — ще немає другого запису, з чим порівняти
    val deltas: Triple<Double, Double, Double>?
        get() = if (entries.size >= 2) Triple(
            entries[0].weightKg - entries[1].weightKg,
            entries[0].waistCm - entries[1].waistCm,
            entries[0].hipsCm - entries[1].hipsCm
        ) else null
}

// «Мозок» екрана: форма + запис у Room + підписка на всі записи
class MeasurementViewModel(
    private val repository: MeasurementRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MeasurementsUiState())
    val uiState: StateFlow<MeasurementsUiState> = _uiState.asStateFlow()

    init {
        // Підписка на записи: додавання/видалення в Room
        // самотужки оновить екран
        viewModelScope.launch {
            repository.observeAll().collect { entries ->
                _uiState.value = _uiState.value.copy(entries = entries)
            }
        }
    }

    // Оновлення полів форми (помилка зникає при наборі)
    fun onWeightChange(v: String) {
        _uiState.value = _uiState.value.copy(weight = v, errorRes = null)
    }
    fun onWaistChange(v: String) {
        _uiState.value = _uiState.value.copy(waist = v, errorRes = null)
    }
    fun onHipsChange(v: String) {
        _uiState.value = _uiState.value.copy(hips = v, errorRes = null)
    }

    // Додавання запису з валідацією (усі три поля обов'язкові)
    fun addEntry() {
        val s = _uiState.value
        val weight = s.weight.toDoubleOrNull()
        if (weight == null) {
            _uiState.value = s.copy(errorRes = R.string.measure_error_weight)
            return
        }
        val waist = s.waist.toDoubleOrNull()
        if (waist == null) {
            _uiState.value = s.copy(errorRes = R.string.measure_error_waist)
            return
        }
        val hips = s.hips.toDoubleOrNull()
        if (hips == null) {
            _uiState.value = s.copy(errorRes = R.string.measure_error_hips)
            return
        }
        viewModelScope.launch {
            repository.add(
                Measurement(
                    date = s.date,
                    weightKg = weight,
                    waistCm = waist,
                    hipsCm = hips
                )
            )
            // Очищення форми після успішного додавання
            _uiState.value = _uiState.value.copy(
                weight = "", waist = "", hips = "", errorRes = null
            )
        }
    }

    // Видалення запису зі списку
    fun deleteEntry(id: Int) {
        viewModelScope.launch { repository.delete(id) }
    }
}