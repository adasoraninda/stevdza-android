package com.codetron.todoapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codetron.todoapp.data.model.ToDoData
import com.codetron.todoapp.data.repo.ToDoRepository
import kotlinx.coroutines.launch

class ToDoViewModel(private val toDoRepository: ToDoRepository) : ViewModel() {

    private val toDoDataSource = MediatorLiveData<List<ToDoData>>()

    val allData: LiveData<List<ToDoData>> get() = toDoDataSource

    init {
        getAllData()
    }

    fun getAllData() {
        toDoDataSource.addSource(toDoRepository.getAllData()) {
            toDoDataSource.postValue(it)
        }
    }

    fun insertData(toDoData: ToDoData) {
        viewModelScope.launch {
            toDoRepository.insertData(toDoData)
        }
    }

    fun deleteData(toDoData: ToDoData) {
        viewModelScope.launch {
            toDoRepository.deleteData(toDoData)
        }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            toDoRepository.deleteAllData()
        }
    }

    fun searchData(searchQuery: String) {
        toDoDataSource.addSource(toDoRepository.searchData(searchQuery)) {
            toDoDataSource.postValue(it)
        }
    }

    fun getListDataByPriority(isHigh: Boolean) {
        toDoDataSource.addSource(toDoRepository.sortDataByHighPriority()) {
            if (isHigh) {
                toDoDataSource.postValue(it)
            } else {
                toDoDataSource.postValue(it.reversed())
            }
        }
    }

}