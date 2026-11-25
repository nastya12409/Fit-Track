package com.example.fitnesstracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Таблиця «Матеріали про спорт»: одна стаття = один рядок
@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,     // заголовок статті
    val category: String,  // ключ категорії (ArticleCategory.name)
    val content: String    // повний текст статті
)