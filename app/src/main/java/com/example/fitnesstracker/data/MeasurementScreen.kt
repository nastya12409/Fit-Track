package com.example.fitnesstracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

// Екран «Заміри»: форма + прогрес проти попереднього запису + список
@Composable
fun MeasurementsScreen(
    navController: NavController,
    viewModel: MeasurementViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Кнопка «Назад» на попередній екран
        Button(onClick = { navController.popBackStack() }) {
            Text(stringResource(R.string.back))
        }

        // Заголовок і дата
        Text(
            text = stringResource(R.string.measure_title),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.measure_today, uiState.date),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        // --- Форма додавання запису ---
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = uiState.weight,
                onValueChange = viewModel::onWeightChange,
                label = { Text(stringResource(R.string.measure_weight)) },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = uiState.waist,
                onValueChange = viewModel::onWaistChange,
                label = { Text(stringResource(R.string.measure_waist)) },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = uiState.hips,
                onValueChange = viewModel::onHipsChange,
                label = { Text(stringResource(R.string.measure_hips)) },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        // Повідомлення про помилку валідації
        uiState.errorRes?.let { res ->
            Text(
                text = stringResource(res),
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }

        Button(
            onClick = viewModel::addEntry,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.measure_add))
        }

        // --- Прогрес проти попереднього запису ---
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = stringResource(R.string.measure_progress),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                val deltas = uiState.deltas
                if (deltas == null) {
                    // Ще один запис — і буде з чим порівняти
                    Text(
                        text = stringResource(R.string.measure_progress_first),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = stringResource(
                            R.string.measure_progress_line,
                            deltas.first,
                            deltas.second,
                            deltas.third
                        ),
                        fontSize = 14.sp
                    )
                }
            }
        }

        // --- Список записів ---
        Text(
            text = stringResource(R.string.measure_entries),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        if (uiState.entries.isEmpty()) {
            Text(
                text = stringResource(R.string.measure_empty),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            uiState.entries.forEach { entry ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = entry.date,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = stringResource(
                                    R.string.measure_line,
                                    entry.weightKg,
                                    entry.waistCm,
                                    entry.hipsCm
                                ),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        TextButton(onClick = { viewModel.deleteEntry(entry.id) }) {
                            Text(stringResource(R.string.measure_delete))
                        }
                    }
                }
            }
        }
    }
}