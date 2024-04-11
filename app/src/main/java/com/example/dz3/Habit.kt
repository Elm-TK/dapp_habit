package com.example.dz3

enum class HabitType {
    GOOD, BAD
}

enum class HabitPriority {
    HIGH, MEDIUM, LOW
}

data class Habit(
    val id: Int,
    val color: Int,
    var title: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val repeat: Int,
    val days: Int
) : java.io.Serializable {
}
