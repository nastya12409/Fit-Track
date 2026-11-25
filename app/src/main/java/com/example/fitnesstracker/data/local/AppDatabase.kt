package com.example.fitnesstracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Версія 4: додано таблицю measurements (заміри).
// Після оновлення кеш перествориться: розділи — з Firestore онлайн,
// статті — з вшитого списку (fallbackToDestructiveMigration у dbModule)
@Database(
    entities = [SectionEntity::class, FoodEntry::class, Article::class, Measurement::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sectionDao(): SectionDao
    abstract fun foodEntryDao(): FoodEntryDao
    abstract fun articleDao(): ArticleDao
    abstract fun measurementDao(): MeasurementDao
}