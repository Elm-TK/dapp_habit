package com.example.dz3.models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.dz3.db.Converters
import java.io.Serializable

enum class HabitType {
    GOOD, BAD
}

enum class HabitPriority {
    HIGH, MEDIUM, LOW
}

@TypeConverters(Converters::class)
@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: String = "",
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "priority") val priority: HabitPriority,
    @ColumnInfo(name = "type") val type: HabitType,
    @ColumnInfo(name = "repeat") val repeat: Int,
    @ColumnInfo(name = "days") val days: Int,
    @ColumnInfo(name = "habitDone") var doneDates: List<Int> = emptyList(),
) : Serializable {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun fromHabitRemote(hr: HabitRemote): Habit {
            return Habit(
                id = hr.uid!!,
                days = hr.count,
                description = hr.description,
                doneDates = hr.done_dates,
                repeat = hr.frequency,
                priority = HabitPriority.entries[hr.priority],
                title = hr.title,
                type = HabitType.entries[hr.type]
            )
        }
    }
}

