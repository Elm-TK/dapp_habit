package com.example.domain.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant

data class HabitRemote(
    var count: Int,
    var date: Int,
    var description: String,
    var done_dates: List<Int>,
    var frequency: Int,
    var priority: Int,
    var title: String,
    var type: Int,
    var uid: String? = null
) {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun fromHabit(habit: Habit, needId: Boolean = true): HabitRemote {
            val habitRemote = HabitRemote(
                count = habit.days,
                date = Instant.now().epochSecond.toInt(),
                description = habit.description,
                done_dates = habit.doneDates,
                frequency = habit.repeat,
                priority = habit.priority.ordinal,
                title = habit.title,
                type = habit.type.ordinal
            )
            if (needId)
                habitRemote.uid = habit.id
            if (habitRemote.description == "") habitRemote.description = " "
            return habitRemote
        }
    }
}