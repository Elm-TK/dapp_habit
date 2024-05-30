package com.example.presentation.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.data.repositories.HabitRepository
import com.example.domain.models.Habit

class HabitViewModel(private val repository: HabitRepository) : ViewModel() {

    val goodHabitsLiveData: LiveData<List<Habit>> = repository.goodHabits.asLiveData()
    val badHabitsLiveData: LiveData<List<Habit>> = repository.badHabits.asLiveData()

    suspend fun getHabitById(id: String): Habit {
        return repository.getHabitById(id)
    }

     fun filterHabits(query: String) {
        repository.filterHabits(query)
    }

    fun sortHabitsByPriorityDescending() {
        repository.sortHabitsByPriorityDescending()
    }

    fun sortHabitsByPriorityAscending() {
        repository.sortHabitsByPriorityAscending()
    }
}
