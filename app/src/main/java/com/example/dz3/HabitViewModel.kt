package com.example.dz3

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HabitViewModel : ViewModel() {

    private val allHabits: MutableList<Habit> = mutableListOf(
        Habit(1, 1, "Чтение книг", "Читать 30 минут каждый день", HabitPriority.HIGH, HabitType.GOOD, 1, 7),
        Habit(2, 2, "Пить воду", "Пить 2 литра воды каждый день", HabitPriority.HIGH, HabitType.GOOD, 1, 7),
        Habit(3, 3, "Курение", "Не курить", HabitPriority.MEDIUM, HabitType.BAD, 1, 7),
        Habit(4, 4, "Заниматься спортом", "Заниматься 3 раза в неделю", HabitPriority.LOW, HabitType.GOOD, 1, 3)
    )

    val habitListLiveData: MutableLiveData<List<Habit>> = MutableLiveData(allHabits)
    val goodHabitsLiveData: MutableLiveData<List<Habit>> = MutableLiveData(getGoodHabits())
    val badHabitsLiveData: MutableLiveData<List<Habit>> = MutableLiveData(getBadHabits())

    fun addHabit(habit: Habit) {
        allHabits.add(habit)
        updateLiveData()
    }

    fun updateHabit(id: Int, habit: Habit) {
        val index = allHabits.indexOfFirst { it.id == id }
        if (index != -1) {
            allHabits[index] = habit
            updateLiveData()
        }
    }

    fun getHabitById(id: Int): Habit? {
        return allHabits.find { it.id == id }
    }

    fun getHabitsByType(type: HabitType): List<Habit> {
        return allHabits.filter { it.type == type }
    }

    fun getLiveDataByType(type: HabitType): MutableLiveData<List<Habit>> {
        return if (type == HabitType.GOOD) {
            goodHabitsLiveData
        } else {
            badHabitsLiveData
        }
    }

    private fun getGoodHabits(): List<Habit> {
        return allHabits.filter { it.type == HabitType.GOOD }
    }

    private fun getBadHabits(): List<Habit> {
        return allHabits.filter { it.type == HabitType.BAD }
    }

    fun filterHabits(query: String) {
        val filteredHabits = allHabits.filter {
            it.title.contains(query, true) || it.description.contains(query, true)
        }
        habitListLiveData.value = filteredHabits
    }

    fun sortHabitsByDate() {
        allHabits.sortByDescending { it.id }
        updateLiveData()
    }

    private fun updateLiveData() {
        habitListLiveData.value = allHabits.toList()
        goodHabitsLiveData.value = getGoodHabits()
        badHabitsLiveData.value = getBadHabits()
    }
}
