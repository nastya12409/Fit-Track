package com.example.fitnesstracker.domain

import com.example.fitnesstracker.MenuSection

// UseCase
class GetSectionsUseCase(private val repository: SectionRepository) {

    suspend operator fun invoke(): List<MenuSection> = repository.getSections()
}