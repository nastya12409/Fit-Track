package com.example.fitnesstracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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

// Назва категорії статті з ресурсів (рядки — у strings.xml, правило рев'юера)
@Composable
private fun categoryLabel(category: String): String = when (category) {
    ArticleCategory.TRAINING.name -> stringResource(R.string.cat_training)
    ArticleCategory.NUTRITION.name -> stringResource(R.string.cat_nutrition)
    ArticleCategory.RECOVERY.name -> stringResource(R.string.cat_recovery)
    else -> category
}

// Екран «Матеріали про спорт»: фільтр категорій + форма + список статей
@Composable
fun ArticlesScreen(
    navController: NavController,
    viewModel: ArticleViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // екран скролиться
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Кнопка «Назад» на попередній екран
        Button(onClick = { navController.popBackStack() }) {
            Text(stringResource(R.string.back))
        }

        // Заголовок екрана
        Text(
            text = stringResource(R.string.articles_title),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        // --- Фільтр за категоріями: «Усі» + 3 категорії ---
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            FilterChip(
                selected = uiState.filter == null,
                onClick = { viewModel.onFilterChange(null) },
                label = { Text(stringResource(R.string.articles_filter_all)) }
            )
            ArticleCategory.entries.forEach { category ->
                FilterChip(
                    selected = uiState.filter == category,
                    onClick = { viewModel.onFilterChange(category) },
                    label = { Text(categoryLabel(category.name)) }
                )
            }
        }

        // --- Форма додавання статті ---
        OutlinedTextField(
            value = uiState.title,
            onValueChange = viewModel::onTitleChange,
            label = { Text(stringResource(R.string.articles_title_field)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.articles_category),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            ArticleCategory.entries.forEach { category ->
                FilterChip(
                    selected = uiState.category == category,
                    onClick = { viewModel.onCategoryChange(category) },
                    label = { Text(categoryLabel(category.name)) }
                )
            }
        }
        OutlinedTextField(
            value = uiState.content,
            onValueChange = viewModel::onContentChange,
            label = { Text(stringResource(R.string.articles_content_field)) },
            minLines = 4, // якщо підсвічує — заміни на Modifier.heightIn(min = 120.dp)
            modifier = Modifier.fillMaxWidth()
        )

        // Повідомлення про помилку валідації
        uiState.errorRes?.let { res ->
            Text(
                text = stringResource(res),
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }

        Button(
            onClick = viewModel::addArticle,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.articles_save))
        }

        // --- Список статей ---
        Text(
            text = stringResource(R.string.articles_heading),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        if (uiState.visibleArticles.isEmpty()) {
            Text(
                text = stringResource(
                    if (uiState.filter == null) R.string.articles_empty
                    else R.string.articles_empty_filter
                ),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            uiState.visibleArticles.forEach { article ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("article/${article.id}") }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = article.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = categoryLabel(article.category),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "›",
                            fontSize = 28.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// Екран читання однієї статті
@Composable
fun ArticleDetailScreen(
    articleId: Int,
    navController: NavController,
    viewModel: ArticleViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // Шукаємо статтю за id у списку
    val article = uiState.articles.firstOrNull { it.id == articleId }

    if (article == null) {
        // Якщо не знайшли — показуємо повідомлення
        Column(modifier = Modifier.padding(16.dp)) {
            Text(stringResource(R.string.article_not_found), fontSize = 18.sp)
            Button(onClick = { navController.popBackStack() }) {
                Text(stringResource(R.string.back))
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // довгий текст скролиться
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Кнопка «Назад»
            Button(onClick = { navController.popBackStack() }) {
                Text(stringResource(R.string.back))
            }

            // Категорія і заголовок
            Text(
                text = categoryLabel(article.category),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = article.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Повний текст статті
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = article.content,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Видалення статті
            Button(
                onClick = {
                    viewModel.deleteArticle(article.id)
                    navController.popBackStack() // повертаємось до списку
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.articles_delete))
            }
        }
    }
}