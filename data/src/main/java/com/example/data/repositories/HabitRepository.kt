package com.example.data.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.domain.models.*
import kotlinx.coroutines.flow.Flow
import java.time.Instant

class HabitRepository(private val habitDao: com.example.data.db.HabitDao) {

    val goodHabits: Flow<List<Habit>> = habitDao.getHabitsByType(HabitType.GOOD)
    val badHabits: Flow<List<Habit>> = habitDao.getHabitsByType(HabitType.BAD)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getHabitsFromServer() {
        try {
            val response = com.example.data.network.RetrofitClient.apiService.getHabits()
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
                    com.example.data.network.RetrofitClient.apiService.putHabit(HabitRemote.fromHabit(habit, false))
                } else com.example.data.network.RetrofitClient.apiService.putHabit(HabitRemote.fromHabit(habit))
            }
        } catch (_: Exception) {
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun insertHabit(habit: Habit) {
        try {
            val response = com.example.data.network.RetrofitClient.apiService.putHabit(HabitRemote.fromHabit(habit, false))
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
        try {
            com.example.data.network.RetrofitClient.apiService.putHabit(HabitRemote.fromHabit(habit))
        } catch (_: Exception) {
        }
        habitDao.updateHabit(habit)
    }

    suspend fun deleteHabit(habit: Habit) {
        try {
            val response = com.example.data.network.RetrofitClient.apiService.deleteHabit(HabitUID(habit.id))
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
            val response = com.example.data.network.RetrofitClient.apiService.markHabitDone(habitDone)
            if (!response.isSuccessful) {
                // отметить позжеw
            }
        } catch (_: Exception) {
        }
        habitDao.markHabitDone(habit)
    }

    suspend fun getHabitById(id: String): Habit {
        return habitDao.getHabitById(id)
    }

    fun filterHabits(query: String): Flow<List<Habit>> {
        return habitDao.filterHabits(query)
    }

    fun sortHabitsByPriorityDescending(): Flow<List<Habit>> {
        return habitDao.getHabitsSortedByPriorityDescending()
    }

    fun sortHabitsByPriorityAscending(): Flow<List<Habit>> {
        return habitDao.getHabitsSortedByPriorityAscending()
    }

    fun getAllHabitsFilteredSorted(filter: String?, sortOrder: SortOrder): Flow<List<Habit>> {
        val query = when (sortOrder) {
            SortOrder.ASCENDING -> "ASC"
            SortOrder.DESCENDING -> "DESC"
        }
        return habitDao.filterAndSortHabits(filter ?: "")
    }

    fun getHabitsByTypeFilteredSorted(type: HabitType, filter: String?, sortOrder: SortOrder): Flow<List<Habit>> {
        val query = when (sortOrder) {
            SortOrder.ASCENDING -> "ASC"
            SortOrder.DESCENDING -> "DESC"
        }
        return habitDao.filterAndSortHabitsByType(type, filter ?: "")
    }

    enum class SortOrder { ASCENDING, DESCENDING }
}
