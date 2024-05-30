package com.example.presentation.view_models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.data.repositories.HabitRepository
import com.example.domain.models.Habit
import com.example.domain.models.HabitPriority
import com.example.domain.models.HabitType
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import kotlinx.coroutines.flow.flowOf

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HabitViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var habitRepository: HabitRepository

    private lateinit var viewModel: HabitViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockitoAnnotations.openMocks(this)
        habitRepository = mock(HabitRepository::class.java)

        val goodHabits = listOf(
            Habit("1", "Test Habit 1", "Test Description", HabitPriority.HIGH, HabitType.GOOD, 3, 4),
            Habit("2", "Test Habit 2", "Test Description", HabitPriority.MEDIUM, HabitType.GOOD, 3, 4),
            Habit("3", "Test Habit 3", "Test Description", HabitPriority.LOW, HabitType.GOOD, 3, 4)
        )

        val badHabits = listOf(
            Habit("4", "Test Habit 4", "Test Description", HabitPriority.HIGH, HabitType.BAD, 3, 4),
            Habit("5", "Test Habit 5", "Test Description", HabitPriority.MEDIUM, HabitType.BAD, 3, 4),
            Habit("6", "Test Habit 6", "Test Description", HabitPriority.LOW, HabitType.BAD, 3, 4)
        )

        Mockito.`when`(habitRepository.goodHabits).thenReturn(flowOf(goodHabits))
        Mockito.`when`(habitRepository.badHabits).thenReturn(flowOf(badHabits))

        viewModel = HabitViewModel(habitRepository)
    }

    @Test
    fun testGetHabitById() = runTest {
        val habit = Habit("1", "Test Habit", "Test Description", HabitPriority.MEDIUM, HabitType.BAD, 3, 4)
        Mockito.`when`(habitRepository.getHabitById("1")).thenReturn(habit)

        val result = viewModel.getHabitById("1")
        assertEquals(habit, result)
    }

    @Test
    fun testFilterHabits() {
        viewModel.filterHabits("Test Query")
        Mockito.verify(habitRepository).filterHabits("Test Query")
    }

    @Test
    fun testSortHabitsByPriorityDescending() {
        viewModel.sortHabitsByPriorityDescending()
        Mockito.verify(habitRepository).sortHabitsByPriorityDescending()
    }

    @Test
    fun testSortHabitsByPriorityAscending() {
        viewModel.sortHabitsByPriorityAscending()
        Mockito.verify(habitRepository).sortHabitsByPriorityAscending()
    }
}
