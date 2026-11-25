package com.example.fitnesstracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.data.local.Article
import com.example.fitnesstracker.domain.ArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Стан екрана «Матеріали про спорт»
data class ArticlesUiState(
    val articles: List<Article> = emptyList(),
    val filter: ArticleCategory? = null, // null = усі категорії
    // Поля форми додавання (рядки — бо це те, що ввів користувач)
    val title: String = "",
    val category: ArticleCategory = ArticleCategory.TRAINING,
    val content: String = "",
    // id рядка помилки з strings.xml або null
    val errorRes: Int? = null
) {
    // Список із урахуванням обраної категорії
    val visibleArticles: List<Article>
        get() = if (filter == null) articles
        else articles.filter { it.category == filter.name }
}

// фільтр + форма + запис у Room + підписка на статті
class ArticleViewModel(
    private val repository: ArticleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArticlesUiState())
    val uiState: StateFlow<ArticlesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Статті вшиті в код — при першому запуску потрапляють у Room
            repository.seedIfEmpty()
            // Підписка: додавання/видалення в Room самотужки оновить екран
            repository.observeAll().collect { articles ->
                _uiState.value = _uiState.value.copy(articles = articles)
            }
        }
    }

    // Вибір категорії для фільтра (null = усі)
    fun onFilterChange(category: ArticleCategory?) {
        _uiState.value = _uiState.value.copy(filter = category)
    }

    // Оновлення полів форми (помилка зникає при наборі)
    fun onTitleChange(v: String) {
        _uiState.value = _uiState.value.copy(title = v, errorRes = null)
    }
    fun onCategoryChange(v: ArticleCategory) {
        _uiState.value = _uiState.value.copy(category = v, errorRes = null)
    }
    fun onContentChange(v: String) {
        _uiState.value = _uiState.value.copy(content = v, errorRes = null)
    }

    // Додавання статті з валідацією
    fun addArticle() {
        val s = _uiState.value
        if (s.title.isBlank()) {
            _uiState.value = s.copy(errorRes = R.string.articles_error_title)
            return
        }
        if (s.content.isBlank()) {
            _uiState.value = s.copy(errorRes = R.string.articles_error_content)
            return
        }
        viewModelScope.launch {
            repository.add(
                Article(
                    title = s.title.trim(),
                    category = s.category.name,
                    content = s.content.trim()
                )
            )
            // Очищення форми після успішного додавання
            _uiState.value = _uiState.value.copy(
                title = "", content = "", errorRes = null
            )
        }
    }

    // Видалення статті зі списку
    fun deleteArticle(id: Int) {
        viewModelScope.launch { repository.delete(id) }
    }
}