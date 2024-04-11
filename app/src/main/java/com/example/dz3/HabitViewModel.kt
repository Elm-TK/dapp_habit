package com.example.dz3

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HabitViewModel : ViewModel() {

    private val allHabits: MutableList<Habit> = mutableListOf(
        Habit(6, 1, "Чтение книг", "Читать 30 минут каждый день", "Высокий", "Хорошая", 1, 7),
        Habit(100, 2, "Пить воду", "Пить 2 литра воды каждый день", "Высокий", "Хорошая", 1, 7),
        Habit(1000, 3, "Курение", "Не курить", "Высокий", "Плохая", 1, 7),
        Habit(30, 4, "Заниматься спортом", "Заниматься 3 раза в неделю", "Высокий", "Хорошая", 1, 3)
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

    fun getHabitsByType(type: String): List<Habit> {
        return allHabits.filter { it.type == type }
    }

    fun getLiveDataByType(type: String): MutableLiveData<List<Habit>> {
        return if (type == "Хорошая") {
            goodHabitsLiveData
        } else {
            badHabitsLiveData
        }
    }

    private fun getGoodHabits(): List<Habit> {
        return allHabits.filter { it.type == "Хорошая" }
    }

    private fun getBadHabits(): List<Habit> {
        return allHabits.filter { it.type == "Плохая" }
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
