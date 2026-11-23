package com.example.fitnesstracker.di

import androidx.room.Room
import com.example.fitnesstracker.HomeViewModel
import com.example.fitnesstracker.data.SectionRepositoryImpl
import com.example.fitnesstracker.data.local.AppDatabase
import com.example.fitnesstracker.domain.SectionRepository
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

// Мережевий клієнт: у варіанті Б це Firestore (аналог Retrofit/Ktor)
val networkModule = module {
    single { FirebaseFirestore.getInstance() }
}

// Локальна база Room і DAO (кеш offline-first)
val dbModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "app.db").build()
    }
    single { get<AppDatabase>().sectionDao() }
}

// Репозиторій: інтерфейс пов'язуємо з реалізацією
val repositoryModule = module {
    single<SectionRepository> { SectionRepositoryImpl(get(), get()) }
}

// ViewModel екрана
val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
}

// Перелік усіх модулів для startKoin
val appModules = listOf(networkModule, dbModule, repositoryModule, viewModelModule)