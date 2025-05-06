/*
package com.example.cleanaf.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanaf.data.model.Task
import com.example.cleanaf.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val repo: TaskRepository) : ViewModel() {
    val tasks: StateFlow<List<Task>> = repo.getTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTask(task: Task) = viewModelScope.launch { repo.addTask(task) }

    fun completeTask(task: Task) = viewModelScope.launch {
        repo.updateTask(task.copy(isCompleted = true, points = task.points + 1))
    }

    fun deleteTask(task: Task) = viewModelScope.launch { repo.deleteTask(task) }
}

*/
