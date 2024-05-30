package com.example.presentation

import android.app.Application
import com.example.presentation.di.AppComponent
import com.example.presentation.di.ContextModule
import com.example.presentation.di.DaggerAppComponent

class App : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.builder().contextModule(ContextModule(this)).build()

    }

    companion object {
        @get:Synchronized
        lateinit var instance: App
            private set
    }
}