package com.example.data.di

import android.content.Context
import com.example.data.db.HabitDao
import com.example.data.db.HabitDatabase
import com.example.data.repositories.HabitRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {
    @Singleton
    @Provides
    fun provideHabitRepository(dao: HabitDao): HabitRepository {
        return HabitRepository(dao)
    }

    @Singleton
    @Provides
    fun provideHabitDatabase(context: Context): HabitDatabase {
        return HabitDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideHabitDao(database: HabitDatabase): HabitDao {
        return database.habitDao()
    }
}