package com.example.fitnesstracker
data class UserProfile( //профіль користувача та інформація про нього
    val id: Int = 1,
    val name: String,
    val age: Int,
    val weightKg: Double,
    val heightCm: Int,
    val gender: Gender,
    val activityLevel: ActivityLevel,
    val goal: Goal
)
data class NutritionLog( // клас для КБЖУ
    val id: Int = 0,
    val date: String,          // дата
    val foodName: String,      // назва продукту
    val calories: Int,         // калорії
    val proteins: Double,      // білки
    val fats: Double,          // жири
    val carbs: Double          // вуглеводи
)
data class WorkoutLog( //запис про тренування
    val id: Int = 0,
    val date: String,
    val workoutType: WorkoutType,
    val durationMinutes: Int,
    val notes: String? = null
)

data class WaterLog( //запис про воду
    val id: Int = 0,
    val date: String,
    val amountMl: Int
)

data class SugarLog( //запис про цукор
    val id: Int = 0,
    val date: String,
    val amountGR: Int
)


enum class Gender { MALE, FEMALE }

enum class ActivityLevel(val factor: Double) {
    SEDENTARY(1.2),      // коли ти нічого не робиш, а тільки сидиш
    LIGHT(1.375),        // легка активність (1-3 тренування в неділю)
    MODERATE(1.55),      // помірна активність (3-5 тренувань)
    HIGH(1.725)          // висока активність
}

enum class Goal {
    WEIGHT_LOSS,         //схуднути (-15% від норми)
    MAINTAIN,            //залишатися в своїй вазі
    MUSCLE_GAIN          //набір маси (+15% до норми)
}

enum class WorkoutType {
    GYM, RUNNING, SWIMMING, YOGA, HOME_WORKOUT, WALKING
}


sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}
fun main() {
    val products = listOf( //список продуктів
        NutritionLog(id = 1, date = "15.10.2026", foodName = "Вівсянка на воді", calories = 150, proteins = 5.0, fats = 3.0, carbs = 27.0),
        NutritionLog(id = 2, date = "15.10.2026", foodName = "Куряча грудка", calories = 165, proteins = 31.0, fats = 3.6, carbs = 0.0),
        NutritionLog(id = 3, date = "15.10.2026", foodName = "Гречка", calories = 132, proteins = 4.5, fats = 1.2, carbs = 25.0),
        NutritionLog(id = 4, date = "15.10.2026", foodName = "Банан", calories = 89, proteins = 1.1, fats = 0.3, carbs = 22.8),
        NutritionLog(id = 5, date = "14.10.2026", foodName = "Салат овочевий", calories = 45, proteins = 1.5, fats = 2.0, carbs = 6.0),
        NutritionLog(id = 6, date = "13.10.2026", foodName = "Чорний шоколад", calories = 230, proteins = 3.0, fats = 15.0, carbs = 24.0)
    )
}

