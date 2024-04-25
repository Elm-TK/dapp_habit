package com.example.dz3

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel


class HabitViewModel(private val repository: HabitRepository) : ViewModel() {

    val habitListLiveData: LiveData<List<Habit>> = repository.allHabits
    val goodHabitsLiveData: LiveData<List<Habit>> = repository.goodHabits
    val badHabitsLiveData: LiveData<List<Habit>> = repository.badHabits


    fun addHabit(habit: Habit) {
        repository.addHabit(habit)
    }

    fun updateHabit(habit: Habit) {
        repository.updateHabit(habit)
    }

    fun getHabitById(id: Long): Habit {
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
