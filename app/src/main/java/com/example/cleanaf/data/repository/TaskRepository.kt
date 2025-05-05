package com.example.cleanaf.data.repository

import com.example.cleanaf.data.local.TaskDao
import com.example.cleanaf.data.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val dao: TaskDao) {
    fun getTasks(): Flow<List<Task>> = dao.getAllTasks()
    suspend fun addTask(task: Task) = dao.insertTask(task)
    suspend fun updateTask(task: Task) = dao.updateTask(task)
    suspend fun deleteTask(task: Task) = dao.deleteTask(task)
}