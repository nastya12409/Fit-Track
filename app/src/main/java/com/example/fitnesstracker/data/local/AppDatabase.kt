package com.example.fitnesstracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SectionEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sectionDao(): SectionDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        // Викликається один раз у MainActivity.onCreate()
        fun init(context: Context) {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app.db"
                ).build()
            }
        }

        // Отримання бази (лише після init)
        fun getInstance(): AppDatabase =
            instance ?: error("AppDatabase не ініціалізовано: викличте init() у MainActivity")
    }
}