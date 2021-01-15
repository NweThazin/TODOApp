package com.example.android.todoapp.data.repository

import com.example.android.todoapp.data.dao.ToDoDao
import com.example.android.todoapp.data.model.Priority
import com.example.android.todoapp.data.model.ToDoData
import io.reactivex.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.mockito.Mockito.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.lang.Exception

class ToDoRepositorySpecs : Spek({
    val mockToDoDao: ToDoDao = mock(ToDoDao::class.java)
    val repository: ToDoRepository by memoized { ToDoRepository(mockToDoDao) }
    describe("#${ToDoRepository::insertData.name}") {
        context("Insert one task") {
            val todoObject = ToDoData(
                id = 100,
                title = "Spek Framework Test",
                priority = Priority.MEDIUM,
                description = "Testing insert TODO data into room db"
            )
            beforeEachTest {
                `when`(mockToDoDao.insertData(todoObject)).thenReturn(Single.just(1))
            }
            it("Inserted a task to DB successfully") {
                runBlocking {
                    repository.insertData(todoObject).test().assertValue(1)
                    verify(mockToDoDao).insertData(todoObject)
                }
            }
        }
        describe("Return Error") {
            val todoObject = ToDoData(
                id = 100,
                title = "Spek Framework Test",
                priority = Priority.MEDIUM,
                description = "Testing insert TODO data into room db"
            )
            val error = Exception("Error Test")
            beforeEachTest {
                reset(mockToDoDao)
                `when`(mockToDoDao.insertData(todoObject)).thenReturn(Single.error(error))
            }
            it("Should emit error") {
                runBlocking {
                    repository.insertData(todoObject).test().assertError(error)
                    verify(mockToDoDao).insertData(todoObject)
                }
            }
        }
    }
})




