package com.example.fitnesstracker.domain

import com.example.fitnesstracker.MenuSection

// ЩО можна отримати, але не ЯК вони зберігаються
interface SectionRepository {
    suspend fun getSections(): List<MenuSection>
}