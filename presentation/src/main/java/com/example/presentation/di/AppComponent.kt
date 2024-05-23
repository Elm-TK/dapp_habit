package com.example.presentation.di

import com.example.data.di.DataModule
import com.example.data.repositories.HabitRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, ContextModule::class])
interface AppComponent {
    fun getHabitRepository(): HabitRepository
}