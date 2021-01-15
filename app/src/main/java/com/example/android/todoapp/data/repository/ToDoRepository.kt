package com.example.android.todoapp.data.repository

import androidx.lifecycle.LiveData
import com.example.android.todoapp.data.dao.ToDoDao
import com.example.android.todoapp.data.model.ToDoData
import io.reactivex.Completable
import io.reactivex.Single


class ToDoRepository(private val toDoDao: ToDoDao) {

    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()

    val sortByHighPriority: LiveData<List<ToDoData>> = toDoDao.sortByHighPriority()

    val sortByLowPriority: LiveData<List<ToDoData>> = toDoDao.sortByLowPriority()

    fun insertData(toDoData: ToDoData): Single<Long> {
        return toDoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData) {
        toDoDao.updateData(toDoData)
    }

    suspend fun deleteItem(toDoData: ToDoData) {
        toDoDao.deleteItem(toDoData)
    }

    suspend fun deleteAll() {
        toDoDao.deleteAll()
    }

    fun searchDatabase(searchQuery: String): LiveData<List<ToDoData>> {
        return toDoDao.searchDatabase(searchQuery)
    }


}