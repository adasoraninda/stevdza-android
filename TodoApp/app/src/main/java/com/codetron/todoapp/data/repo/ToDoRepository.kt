package com.codetron.todoapp.data.repo

import androidx.lifecycle.LiveData
import com.codetron.todoapp.data.model.ToDoData

interface ToDoRepository {

    fun getAllData(): LiveData<List<ToDoData>>

    suspend fun insertData(toDoData: ToDoData)

    suspend fun deleteData(toDoData: ToDoData)

    suspend fun deleteAllData()

    fun searchData(searchQuery: String): LiveData<List<ToDoData>>

    fun sortDataByHighPriority(): LiveData<List<ToDoData>>

}