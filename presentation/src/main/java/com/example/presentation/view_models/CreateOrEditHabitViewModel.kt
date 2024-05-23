package com.example.presentation.view_models

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repositories.HabitRepository
import com.example.domain.models.Habit
import com.example.domain.models.HabitPriority
import com.example.domain.models.HabitType
import com.example.presentation.App
import kotlinx.coroutines.launch

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
                if (habit.type == HabitType.GOOD) {
                    if (habit.doneDates.count() >= habit.repeat) {
                        Toast.makeText(App.instance.baseContext, "You are breathtaking!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            App.instance.baseContext,
                            "Стоит выполнить еще ${habit.repeat - habit.doneDates.count()} раз",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    if (habit.doneDates.count() >= habit.repeat) {
                        Toast.makeText(App.instance.baseContext, "Хватит это делать", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            App.instance.baseContext,
                            "Можете выполнить еще ${habit.repeat - habit.doneDates.count()} раз",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            _habitSavedEvent.value = true
        }
    }

    fun isValidInput(title: String, repeat: String, days: String): Boolean {
        return title.isNotBlank() && repeat.toIntOrNull() != null && repeat.toInt() > 0 &&
                days.toIntOrNull() != null && days.toInt() > 0
    }
}
