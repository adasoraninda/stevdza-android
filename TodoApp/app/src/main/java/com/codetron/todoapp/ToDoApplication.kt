package com.codetron.todoapp

import android.app.Application
import com.codetron.todoapp.di.component.AppComponent
import com.codetron.todoapp.di.component.DaggerAppComponent

class ToDoApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

}