package com.example.data.db

import androidx.room.*
import com.example.domain.models.Habit
import com.example.domain.models.HabitType
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits")
    fun getAllHabits(): List<Habit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(habits: List<Habit>)

    @Query("SELECT * FROM habits WHERE type = :type")
    fun getHabitsByType(type: HabitType): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: String): Habit

    @Query("SELECT * FROM habits WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun filterHabits(query: String): Flow<List<Habit>>

    @Query("SELECT * FROM habits ORDER BY priority DESC")
    fun getHabitsSortedByPriorityDescending(): Flow<List<Habit>>

    @Query("SELECT * FROM habits ORDER BY priority ASC")
    fun getHabitsSortedByPriorityAscending(): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Update
    suspend fun markHabitDone(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM habits WHERE (title LIKE '%' || :filter || '%' OR description LIKE '%' || :filter || '%') ORDER BY priority")
    fun filterAndSortHabits(filter: String): Flow<List<Habit>>

    @Query("""
        SELECT * FROM habits
        WHERE type = :type
        AND (title LIKE '%' || :filter || '%' OR description LIKE '%' || :filter || '%')
        ORDER BY
        CASE WHEN :sortOrder = 'ASC' THEN priority END ASC,
        CASE WHEN :sortOrder = 'DESC' THEN priority END DESC
    """)
    fun filterAndSortHabitsByType(
        type: HabitType,
        filter: String,
        sortOrder: String
    ): Flow<List<Habit>>
}
