package com.example.dz3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class HabitViewModel(private val repository: HabitRepository) : ViewModel() {
    private var _query = ""
    private var _sort = SortOrder.ASCENDING
    var habitListLiveData: MutableLiveData<List<Habit>> = MutableLiveData()
    var goodHabitsLiveData: MutableLiveData<List<Habit>> = MutableLiveData()
    var badHabitsLiveData: MutableLiveData<List<Habit>> = MutableLiveData()

    init {
        updateLiveData()
    }

    suspend fun addHabit(habit: Habit) {
        repository.addHabit(habit)
    }

    suspend fun updateHabit(habit: Habit) {
        repository.updateHabit(habit)
    }

    suspend fun getHabitById(id: Long): Habit {
        return repository.getHabitById(id)
    }

    fun filterHabits(query: String) {
        _query = query
//        repository.filterHabits(query)
        updateLiveData()
    }

    fun sortHabitsByPriorityDescending() {
        _sort = SortOrder.DESCENDING
//        repository.sortHabitsByPriorityDescending()
        updateLiveData()
    }

    fun sortHabitsByPriorityAscending() {
        _sort = SortOrder.ASCENDING
//        repository.sortHabitsByPriorityAscending()
        updateLiveData()
    }

    fun updateLiveData() {
        var a = repository.getAllHabitsFilteredSorted(_query, _sort).value
        var b = repository.getHabitsByTypeFilteredSorted(HabitType.GOOD, _query, _sort).value
        var c = repository.getHabitsByTypeFilteredSorted(HabitType.BAD, _query, _sort).value
        habitListLiveData.value = repository.getAllHabitsFilteredSorted(_query, _sort).value
        goodHabitsLiveData.value = repository.getHabitsByTypeFilteredSorted(HabitType.GOOD, _query, _sort).value
        badHabitsLiveData.value = repository.getHabitsByTypeFilteredSorted(HabitType.BAD, _query, _sort).value
    }
}

enum class SortOrder { ASCENDING, DESCENDING }
