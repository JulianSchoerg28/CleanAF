package com.example.cleanaf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanaf.data.Task
import com.example.cleanaf.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val uiState = repository.getAllTasks()
        .map { TaskUiState(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskUiState())

    fun addTask(title: String, description: String) {
        val newTask = Task(title = title, description = description)
        viewModelScope.launch {
            repository.insert(newTask)
        }
    }

    fun getTaskById(taskId: Int): Flow<Task?> {
        return repository.getTaskById(taskId)
    }


}

data class TaskUiState(
    val taskList: List<Task> = emptyList()
)
