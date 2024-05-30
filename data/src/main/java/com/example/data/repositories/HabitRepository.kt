package com.example.data.repositories

import android.util.Log
import com.example.data.db.HabitDao
import com.example.data.network.RetrofitClient
import com.example.domain.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import java.time.Instant

class HabitRepository(private val habitDao: HabitDao) {

    private var query: String = ""
    private var sortOrder: SortOrder = SortOrder.ASCENDING
    var goodHabits: Flow<List<Habit>> = habitDao.getHabitsByType(HabitType.GOOD)
    var badHabits: Flow<List<Habit>> = habitDao.getHabitsByType(HabitType.BAD)

    suspend fun getHabitsFromServer() {
        try {
            val response = RetrofitClient.apiService.getHabits()
            if (response.isSuccessful) {
                response.body()?.let {
                    val habits = it.map { habit -> Habit.fromHabitRemote(habit) }
                    habitDao.insertAll(habits)
                }
            }
        } catch (_: Exception) {
        }
    }

    suspend fun syncHabitsToServer() {
        try {
            habitDao.getAllHabits().forEach { habit ->
                if (habit.id.length != 36) {
                    RetrofitClient.apiService.putHabit(HabitRemote.fromHabit(habit, false))
                } else RetrofitClient.apiService.putHabit(HabitRemote.fromHabit(habit))
            }
        } catch (_: Exception) {
        }
    }

    suspend fun insertHabit(habit: Habit) {
        try {
            val response = RetrofitClient.apiService.putHabit(HabitRemote.fromHabit(habit, false))
            if (response.isSuccessful) {
                response.body()?.let { responseBody ->
                    habit.id = responseBody.uid
                }
            }
        } catch (_: Exception) {
        }
        habitDao.insertHabit(habit)
    }

    suspend fun updateHabit(habit: Habit) {
        try {
            RetrofitClient.apiService.putHabit(HabitRemote.fromHabit(habit))
        } catch (_: Exception) {
        }
        habitDao.updateHabit(habit)
    }

    suspend fun deleteHabit(habit: Habit) {
        try {
            val response = RetrofitClient.apiService.deleteHabit(HabitUID(habit.id))
            if (!response.isSuccessful) {
                // удалить позже
            }
        } catch (_: Exception) {
        }
        habitDao.deleteHabit(habit)
    }

    suspend fun markHabitDone(habit: Habit) {
        try {
            val habitDone = HabitDone(Instant.now().epochSecond.toInt(), habit.id)
            habit.doneDates += habitDone.date
            val response = RetrofitClient.apiService.markHabitDone(habitDone)
            if (!response.isSuccessful) {
                // отметить позже
            }
        } catch (_: Exception) {
        }
        habitDao.markHabitDone(habit)
    }

    suspend fun getHabitById(id: String): Habit {
        return habitDao.getHabitById(id)
    }

    fun filterHabits(query: String): Flow<List<Habit>> {
        setQuery(query)
        return habitDao.filterHabits(query)
    }

    fun sortHabitsByPriorityDescending(): Flow<List<Habit>> {
        return habitDao.getHabitsSortedByPriorityDescending()
    }

    fun sortHabitsByPriorityAscending(): Flow<List<Habit>> {
        return habitDao.getHabitsSortedByPriorityAscending()
    }

//    fun getAllHabitsFilteredSorted(filter: String?, sortOrder: SortOrder): Flow<List<Habit>> {
//        val query = when (sortOrder) {
//            SortOrder.ASCENDING -> "ASC"
//            SortOrder.DESCENDING -> "DESC"
//        }
//        return habitDao.filterAndSortHabits(filter ?: "")
//    }
//
//    fun getHabitsByTypeFilteredSorted(type: HabitType, filter: String?, sortOrder: SortOrder) {
//        val query = when (sortOrder) {
//            SortOrder.ASCENDING -> "ASC"
//            SortOrder.DESCENDING -> "DESC"
//        }
//        val a = habitDao.filterAndSortHabitsByType(type, filter ?: "")
//        Log.i("filtered", a.toString())
//        goodHabits = a
//    }

    private fun getFilteredSortedHabits(type: HabitType): Flow<List<Habit>> {
        val sortOrderString = when (sortOrder) {
            SortOrder.ASCENDING -> "ASC"
            SortOrder.DESCENDING -> "DESC"
        }
        return habitDao.filterAndSortHabitsByType(type, query, sortOrderString)
    }

    fun setQuery(query: String) {
        this.query = query
        refreshHabits()
    }

    fun setSortOrder(sortOrder: SortOrder) {
        this.sortOrder = sortOrder
        refreshHabits()
    }

    private fun refreshHabits() {
        goodHabits = getFilteredSortedHabits(HabitType.GOOD)
        badHabits = getFilteredSortedHabits(HabitType.BAD)
    }

    enum class SortOrder { ASCENDING, DESCENDING }
}
