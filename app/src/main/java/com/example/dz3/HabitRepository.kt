package com.example.dz3

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData

class HabitRepository(private val habitDao: HabitDao) {

    val allHabits: LiveData<List<Habit>> = habitDao.getAllHabits()
    val goodHabits: LiveData<List<Habit>> = habitDao.getHabitsByType(HabitType.GOOD)
    val badHabits: LiveData<List<Habit>> = habitDao.getHabitsByType(HabitType.BAD)

    suspend fun addHabit(habit: Habit) {
        habitDao.insertHabit(habit)
    }

    suspend fun updateHabit(habit: Habit) {
        habitDao.updateHabit(habit)
    }

    suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit)
    }

    suspend fun getHabitById(id: Long): Habit {
        return habitDao.getHabitById(id)
    }

    fun filterHabits(query: String): LiveData<List<Habit>> {
        return habitDao.filterHabits(query)
    }

    fun sortHabitsByPriorityDescending(): LiveData<List<Habit>> {
        return habitDao.getHabitsSortedByPriorityDescending()
    }

    fun sortHabitsByPriorityAscending(): LiveData<List<Habit>> {
        return habitDao.getHabitsSortedByPriorityAscending()
    }

    fun getAllHabitsFilteredSorted(filter: String?, sortOrder: SortOrder): LiveData<List<Habit>> {
        val query = when (sortOrder) {
            SortOrder.ASCENDING -> "ASC"
            SortOrder.DESCENDING -> "DESC"
        }
        return habitDao.filterAndSortHabits(filter ?: "")
    }

    fun getHabitsByTypeFilteredSorted(type: HabitType, filter: String?, sortOrder: SortOrder): LiveData<List<Habit>> {
        val query = when (sortOrder) {
            SortOrder.ASCENDING -> "ASC"
            SortOrder.DESCENDING -> "DESC"
        }
        return habitDao.filterAndSortHabitsByType(type, filter ?: "")
    }
}
