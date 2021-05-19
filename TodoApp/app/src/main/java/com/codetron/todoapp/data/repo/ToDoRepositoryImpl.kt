package com.codetron.todoapp.data.repo

import androidx.lifecycle.LiveData
import com.codetron.todoapp.data.db.ToDoDao
import com.codetron.todoapp.data.model.ToDoData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToDoRepositoryImpl @Inject constructor(private val toDoDao: ToDoDao) : ToDoRepository {

    override fun getAllData(): LiveData<List<ToDoData>> {
        return toDoDao.getAllData()
    }

    override suspend fun insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData)
    }

    override suspend fun deleteData(toDoData: ToDoData) {
        toDoDao.deleteData(toDoData)
    }

    override suspend fun deleteAllData() {
        toDoDao.deleteAllData()
    }

    override fun searchData(searchQuery: String): LiveData<List<ToDoData>> {
        return toDoDao.searchData(searchQuery)
    }

    override fun sortDataByHighPriority(): LiveData<List<ToDoData>> {
        return toDoDao.sortDataByHighPriority()
    }
}