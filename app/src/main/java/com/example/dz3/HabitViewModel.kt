package com.example.dz3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class HabitViewModel(private val repository: HabitRepository) : ViewModel() {
    private var _query = ""
    private var _sort = SortOrder.ASCENDING

    val habitListLiveData: LiveData<List<Habit>> = repository.allHabits
    val goodHabitsLiveData: LiveData<List<Habit>> = repository.goodHabits
    val badHabitsLiveData: LiveData<List<Habit>> = repository.badHabits


//    var habitListLiveData: MutableLiveData<List<Habit>> = MutableLiveData()
//    var goodHabitsLiveData: MutableLiveData<List<Habit>> = MutableLiveData()
//    var badHabitsLiveData: MutableLiveData<List<Habit>> = MutableLiveData()

//    init {
//        repository.allHabits.observeForever { _ -> updateLiveData() }
//        repository.goodHabits.observeForever { _ -> updateLiveData() }
//        repository.badHabits.observeForever { _ -> updateLiveData() }
//        habitListLiveData.value = repository.allHabits.value
//        goodHabitsLiveData.value = repository.goodHabits.value
//        badHabitsLiveData.value = repository.badHabits.value
//
//        updateLiveData()
//    }

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

    private fun updateLiveData() {
//        habitListLiveData.value = repository.getAllHabitsFilteredSorted(_query, _sort).value
//        goodHabitsLiveData.value = repository.getHabitsByTypeFilteredSorted(HabitType.GOOD, _query, _sort).value
//        badHabitsLiveData.value = repository.getHabitsByTypeFilteredSorted(HabitType.BAD, _query, _sort).value
    }
}

enum class SortOrder { ASCENDING, DESCENDING }
