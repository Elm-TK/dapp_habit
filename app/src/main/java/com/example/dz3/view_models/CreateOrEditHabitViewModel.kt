package com.example.dz3.view_models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz3.models.Habit
import com.example.dz3.models.HabitPriority
import com.example.dz3.models.HabitType
import com.example.dz3.db.HabitRepository
import com.example.dz3.models.HabitDone
import kotlinx.coroutines.launch
import java.time.Instant

class CreateOrEditHabitViewModel(private val habitRepository: HabitRepository) : ViewModel() {

    private val _habitSavedEvent = MutableLiveData<Boolean>()
    val habitSavedEvent: LiveData<Boolean> = _habitSavedEvent

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveHabit(
        habit: Habit?,
        isEdit: Boolean,
        title: String,
        description: String,
        repeat: Int,
        days: Int,
        priority: HabitPriority,
        type: HabitType
    ) {
        val newHabit = habit?.copy(
            title = title,
            description = description,
            priority = priority,
            type = type,
            repeat = repeat,
            days = days
        ) ?: Habit(
            "${title.hashCode() + repeat.hashCode() + days.hashCode()}",
            title,
            description,
            priority,
            type,
            repeat,
            days
        )

        viewModelScope.launch {
            if (isEdit && habit != null) {
                habitRepository.updateHabit(newHabit)
            } else {
                habitRepository.insertHabit(newHabit)
            }
            _habitSavedEvent.value = true
        }
    }

    fun deleteHabit(habit: Habit?) {
        viewModelScope.launch {
            if (habit != null) {
                habitRepository.deleteHabit(habit)
            }
            _habitSavedEvent.value = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun markHabitDone(habit: Habit?) {
        viewModelScope.launch {
            if (habit != null) {
                habitRepository.markHabitDone(habit)
            }
            _habitSavedEvent.value = true
        }
    }

    fun isValidInput(title: String, repeat: String, days: String): Boolean {
        return title.isNotBlank() && repeat.toIntOrNull() != null && repeat.toInt() > 0 &&
                days.toIntOrNull() != null && days.toInt() > 0
    }
}
