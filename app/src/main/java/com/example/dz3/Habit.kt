package com.example.dz3

import androidx.room.ColumnInfo
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
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "priority")  val priority: HabitPriority,
    @ColumnInfo(name = "type") val type: HabitType,
    @ColumnInfo(name = "repeat") val repeat: Int,
    @ColumnInfo(name = "days")val days: Int
) : Serializable

