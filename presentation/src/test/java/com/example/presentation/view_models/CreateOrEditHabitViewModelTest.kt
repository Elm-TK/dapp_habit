package com.example.presentation.view_models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.data.repositories.HabitRepository
import com.example.domain.models.Habit
import com.example.domain.models.HabitPriority
import com.example.domain.models.HabitType
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.junit.Test


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CreateOrEditHabitViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var habitRepository: HabitRepository

    private lateinit var viewModel: CreateOrEditHabitViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        habitRepository = mock(HabitRepository::class.java)
        viewModel = CreateOrEditHabitViewModel(habitRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun saveHabit_insertNewHabit() = runTest {
        val habit = null
        val title = "Test Habit"
        val description = "Test Description"
        val repeat = 5
        val days = 7
        val priority = HabitPriority.HIGH
        val type = HabitType.GOOD

        viewModel.saveHabit(habit, false, title, description, repeat, days, priority, type)

        advanceUntilIdle()
        val newHabit = Habit(
            (title.hashCode() + repeat.hashCode() + days.hashCode()).toString(),
            title,
            description,
            priority,
            type,
            repeat,
            days
        )
        verify(habitRepository).insertHabit(newHabit)
        assertEquals(true, viewModel.habitSavedEvent.value)
    }

    @Test
    fun saveHabit_updateHabit() = runTest {
        val habit = Habit("1", "Old Title", "Old Description", HabitPriority.LOW, HabitType.BAD, 3, 4)
        val title = "New Title"
        val description = "New Description"
        val repeat = 5
        val days = 7
        val priority = HabitPriority.HIGH
        val type = HabitType.GOOD

        viewModel.saveHabit(habit, true, title, description, repeat, days, priority, type)

        advanceUntilIdle()

        val updatedHabit = Habit("1", title, description, priority, type, repeat, days)
        verify(habitRepository).updateHabit(updatedHabit)
        assertEquals(true, viewModel.habitSavedEvent.value)
    }

    @Test
    fun deleteHabit_deleteHabit() = runTest {
        val habit = Habit("1", "Test Habit", "Test Description", HabitPriority.MEDIUM, HabitType.BAD, 3, 4)

        viewModel.deleteHabit(habit)

        advanceUntilIdle()

        verify(habitRepository).deleteHabit(habit)
        assertEquals(true, viewModel.habitSavedEvent.value)
    }

    @Test
    fun isValidInput_validInput() {
        val title = "Valid Title"
        val repeat = "5"
        val days = "7"

        val result = viewModel.isValidInput(title, repeat, days)

        assertTrue(result)
    }

    @Test
    fun isValidInput_invalidTitle() {
        val title = ""
        val repeat = "5"
        val days = "7"

        val result = viewModel.isValidInput(title, repeat, days)

        assertFalse(result)
    }

    @Test
    fun isValidInput_invalidRepeat() {
        val title = "Valid Title"
        val repeat = "0"
        val days = "7"

        val result = viewModel.isValidInput(title, repeat, days)

        assertFalse(result)
    }

    @Test
    fun isValidInput_invalidDays() {
        val title = "Valid Title"
        val repeat = "5"
        val days = "0"

        val result = viewModel.isValidInput(title, repeat, days)

        assertFalse(result)
    }

    @Test
    fun isValidInput_emptyRepeat() {
        val title = "Valid Title"
        val repeat = ""
        val days = "7"

        val result = viewModel.isValidInput(title, repeat, days)

        assertFalse(result)
    }

    @Test
    fun isValidInput_emptyDays() {
        val title = "Valid Title"
        val repeat = "5"
        val days = ""

        val result = viewModel.isValidInput(title, repeat, days)

        assertFalse(result)
    }

    @Test
    fun isValidInput_negativeRepeat() {
        val title = "Valid Title"
        val repeat = "-3"
        val days = "7"

        val result = viewModel.isValidInput(title, repeat, days)

        assertFalse(result)
    }

    @Test
    fun isValidInput_negativeDays() {
        val title = "Valid Title"
        val repeat = "5"
        val days = "-5"

        val result = viewModel.isValidInput(title, repeat, days)

        assertFalse(result)
    }
}