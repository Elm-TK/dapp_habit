package com.example.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory<T>(private val creator: () -> T) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return creator()?.takeIf { modelClass.isAssignableFrom(it::class.java) } as? T
            ?: throw IllegalArgumentException("Unknown ViewModel class")
    }
}

