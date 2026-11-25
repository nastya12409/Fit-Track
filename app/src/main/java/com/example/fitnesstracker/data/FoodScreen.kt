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

// Екран «Щоденник харчування»: форма + ітого за день + список записів
@Composable
fun FoodScreen(
    navController: NavController,
    viewModel: FoodViewModel = koinViewModel()
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
            text = stringResource(R.string.food_title),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.food_today, uiState.date),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        // --- Форма додавання запису ---
        OutlinedTextField(
            value = uiState.name,
            onValueChange = viewModel::onNameChange,
            label = { Text(stringResource(R.string.food_name)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = uiState.calories,
                onValueChange = viewModel::onCaloriesChange,
                label = { Text(stringResource(R.string.food_calories)) },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = uiState.proteins,
                onValueChange = viewModel::onProteinsChange,
                label = { Text(stringResource(R.string.food_proteins)) },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = uiState.fats,
                onValueChange = viewModel::onFatsChange,
                label = { Text(stringResource(R.string.food_fats)) },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = uiState.carbs,
                onValueChange = viewModel::onCarbsChange,
                label = { Text(stringResource(R.string.food_carbs)) },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = uiState.water,
                onValueChange = viewModel::onWaterChange,
                label = { Text(stringResource(R.string.food_water)) },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = uiState.sugar,
                onValueChange = viewModel::onSugarChange,
                label = { Text(stringResource(R.string.food_sugar)) },
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
            Text(stringResource(R.string.food_add))
        }

        // --- Ітого за день ---
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = stringResource(R.string.food_total),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = stringResource(
                        R.string.food_line,
                        uiState.totalCalories,
                        uiState.totalProteins,
                        uiState.totalFats,
                        uiState.totalCarbs,
                        uiState.totalWater,
                        uiState.totalSugar
                    ),
                    fontSize = 14.sp
                )
            }
        }

        // --- Список записів ---
        Text(
            text = stringResource(R.string.food_entries),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        if (uiState.entries.isEmpty()) {
            Text(
                text = stringResource(R.string.food_empty),
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
                                text = entry.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = stringResource(
                                    R.string.food_line,
                                    entry.calories,
                                    entry.proteins,
                                    entry.fats,
                                    entry.carbs,
                                    entry.water,
                                    entry.sugar
                                ),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        TextButton(onClick = { viewModel.deleteEntry(entry.id) }) {
                            Text(stringResource(R.string.food_delete))
                        }
                    }
                }
            }
        }
    }
}