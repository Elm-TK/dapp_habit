package com.example.dz3

import androidx.lifecycle.LiveData

class HabitRepository(private val habitDao: HabitDao) {

    var allHabits: LiveData<List<Habit>> = habitDao.getAllHabits()
    var goodHabits: LiveData<List<Habit>> = habitDao.getHabitsByType(HabitType.GOOD)
    var badHabits: LiveData<List<Habit>> = habitDao.getHabitsByType(HabitType.BAD)

    fun addHabit(habit: Habit) {
        habitDao.insertHabit(habit)
    }

    fun updateHabit(habit: Habit) {
        habitDao.updateHabit(habit)
    }

    fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit)
    }


    fun getHabitById(id: Long): Habit {
        return habitDao.getHabitById(id)
    }

    fun filterHabits(query: String) {
//        allHabits = _allhabits.value?.filter { it.id == 0 }
    }

    fun sortHabitsByPriorityDescending() {

    }

    fun sortHabitsByPriorityAscending() {

    }
}

