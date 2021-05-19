package com.codetron.todoapp.di.module

import com.codetron.todoapp.data.repo.ToDoRepository
import com.codetron.todoapp.data.repo.ToDoRepositoryImpl
import dagger.Binds
import dagger.Module

@Module(includes = [DatabaseModule::class])
abstract class RepositoryModule {

    @Binds
    abstract fun provideToDoRepository(toDoRepositoryImpl: ToDoRepositoryImpl): ToDoRepository
}