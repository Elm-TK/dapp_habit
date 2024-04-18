package com.example.dz3

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

enum class HabitType {
    GOOD, BAD
}

enum class HabitPriority {
    HIGH, MEDIUM, LOW
}

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val title: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val repeat: Int,
    val days: Int
) : Serializable

