package com.example.presentation.view_models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.data.repositories.HabitRepository
import com.example.domain.models.Habit

@RequiresApi(Build.VERSION_CODES.O)
class HabitViewModel(private val repository: HabitRepository) : ViewModel() {
    private var _query = ""
    private var _sort = SortOrder.ASCENDING

    val goodHabitsLiveData: LiveData<List<Habit>> = repository.goodHabits.asLiveData()
    val badHabitsLiveData: LiveData<List<Habit>> = repository.badHabits.asLiveData()


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
        repository.insertHabit(habit)
    }

    suspend fun updateHabit(habit: Habit) {
        repository.updateHabit(habit)
    }

    suspend fun getHabitById(id: String): Habit {
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
