package com.example.fitnesstracker.di

import androidx.room.Room
import com.example.fitnesstracker.ArticleViewModel
import com.example.fitnesstracker.FoodViewModel
import com.example.fitnesstracker.HomeViewModel
import com.example.fitnesstracker.MeasurementViewModel
import com.example.fitnesstracker.data.ArticleRepositoryImpl
import com.example.fitnesstracker.data.FoodRepositoryImpl
import com.example.fitnesstracker.data.MeasurementRepositoryImpl
import com.example.fitnesstracker.data.SectionRepositoryImpl
import com.example.fitnesstracker.data.local.AppDatabase
import com.example.fitnesstracker.domain.ArticleRepository
import com.example.fitnesstracker.domain.FoodRepository
import com.example.fitnesstracker.domain.MeasurementRepository
import com.example.fitnesstracker.domain.SectionRepository
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

// Мережевий клієнт: у варіанті Б це Firestore (аналог Retrofit/Ktor)
val networkModule = module {
    single { FirebaseFirestore.getInstance() }
}

// Локальна база Room і DAO (offline-first кеш + записи користувача)
val dbModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "app.db")
            .fallbackToDestructiveMigration() // схема v4: старий кеш оновиться
            .build()
    }
    single { get<AppDatabase>().sectionDao() }
    single { get<AppDatabase>().foodEntryDao() }
    single { get<AppDatabase>().articleDao() }
    single { get<AppDatabase>().measurementDao() }
}

// Репозиторії: інтерфейс - реалізація
val repositoryModule = module {
    single<SectionRepository> { SectionRepositoryImpl(get(), get()) }
    single<FoodRepository> { FoodRepositoryImpl(get()) }
    single<ArticleRepository> { ArticleRepositoryImpl(get()) }
    single<MeasurementRepository> { MeasurementRepositoryImpl(get()) }
}

// ViewModel екранів
val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { FoodViewModel(get()) }
    viewModel { ArticleViewModel(get()) }
    viewModel { MeasurementViewModel(get()) }
}

// Перелік усіх модулів для startKoin
val appModules = listOf(networkModule, dbModule, repositoryModule, viewModelModule)