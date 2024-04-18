package com.example.dz3

import androidx.lifecycle.LiveData

class HabitRepository(private val habitDao: HabitDao) {

//    var _allhabits: LiveData<List<Habit>> = habitDao.getAllHabits()

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


    fun getHabitById(id: Long, type: HabitType): Habit? {
//        return habitDao.getHabitById(id).value
        if (type == HabitType.GOOD) {
            return goodHabits.value?.find { it.id == id }
        } else if (type == HabitType.BAD) {
            return badHabits.value?.find { it.id == id }
        } else return null
    }

    fun filterHabits(query: String) {
//        allHabits = _allhabits.value?.filter { it.id == 0 }
    }

    fun sortHabitsByPriorityDescending() {

    }

    fun sortHabitsByPriorityAscending() {

    }
}

