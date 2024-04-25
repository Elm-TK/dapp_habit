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
    fun getHabitById(id: Long): Habit

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHabit(habit: Habit)

    @Update
    fun updateHabit(habit: Habit)

    @Delete
    fun deleteHabit(habit: Habit)
}
