package com.example.dz3

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits")
    fun getAllHabits(): LiveData<List<Habit>>

    @Query("SELECT * FROM habits WHERE type = :type")
    fun getHabitsByType(type: HabitType): LiveData<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: Long): Habit

    @Query("SELECT * FROM habits WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun filterHabits(query: String): LiveData<List<Habit>>

    @Query("SELECT * FROM habits ORDER BY priority DESC")
    fun getHabitsSortedByPriorityDescending(): LiveData<List<Habit>>

    @Query("SELECT * FROM habits ORDER BY priority ASC")
    fun getHabitsSortedByPriorityAscending(): LiveData<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM habits WHERE (title LIKE '%' || :filter || '%' OR description LIKE '%' || :filter || '%') ORDER BY priority")
    fun filterAndSortHabits(filter: String): LiveData<List<Habit>>


    @Query("SELECT * FROM habits WHERE type = :type AND (title LIKE '%' || :filter || '%' OR description LIKE '%' || :filter || '%') ORDER BY priority")
    fun filterAndSortHabitsByType(type: HabitType, filter: String): LiveData<List<Habit>>

}
