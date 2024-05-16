package com.example.dz3.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dz3.db.HabitRepository

class CreateOrEditHabitViewModelFactory(private val habitRepository: HabitRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateOrEditHabitViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateOrEditHabitViewModel(habitRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
