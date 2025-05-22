package com.example.cleanaf.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val dao: TaskDao) {
    fun getAllTasks(): Flow<List<Task>> = dao.getAllTasks()

    fun getTaskById(id: Int): Flow<Task?> {
        return dao.getTaskById(id)
    }

    suspend fun insert(task: Task) = dao.insert(task)
    suspend fun update(task: Task) = dao.update(task)
    suspend fun delete(task: Task) = dao.delete(task)
}
