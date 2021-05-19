package com.codetron.todoapp.di.module

import android.content.Context
import androidx.room.Room
import com.codetron.todoapp.data.db.ToDoDao
import com.codetron.todoapp.data.db.ToDoDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideToDoDatabase(context: Context): ToDoDatabase {
        return Room.databaseBuilder(
            context,
            ToDoDatabase::class.java,
            "todo_database"
        ).build()
    }

    @Provides
    fun provideToDoDao(toDoDatabase: ToDoDatabase): ToDoDao {
        return toDoDatabase.toDoDao()
    }


}