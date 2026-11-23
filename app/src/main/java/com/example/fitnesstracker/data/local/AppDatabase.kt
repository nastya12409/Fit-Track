package com.example.fitnesstracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Більше немає init()/getInstance() — базу створює Koin (dbModule)
@Database(entities = [SectionEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sectionDao(): SectionDao
}