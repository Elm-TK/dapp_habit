package com.example.dz3.db

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.dz3.models.*
import com.example.dz3.network.RetrofitClient
import com.example.dz3.view_models.SortOrder
import java.time.Instant

class HabitRepository(private val habitDao: HabitDao) {

    val goodHabits: LiveData<List<Habit>> = habitDao.getHabitsByType(HabitType.GOOD)
    val badHabits: LiveData<List<Habit>> = habitDao.getHabitsByType(HabitType.BAD)

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
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


    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateHabit(habit: Habit) {
        deleteHabit(habit)
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

    @RequiresApi(Build.VERSION_CODES.O)
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
