package com.example.cleanaf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanaf.data.Task
import com.example.cleanaf.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val uiState: StateFlow<TaskUiState> = repository.getAllTasks()
        .map { taskList -> TaskUiState(taskList) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskUiState())

    val tasks: StateFlow<List<Task>> = repository.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun update(task: Task) {
        viewModelScope.launch {
            repository.update(task)
        }
    }


    fun insertTask(
        name: String,
        description: String,
        date: String,
        time: String,
        interval: Int,
        points: Int
    ) {
        viewModelScope.launch {
            val newTask = Task(
                name = name,
                description = description,
                date = date,
                time = time,
                interval = interval,
                points = points,
                isDone = false
            )
            repository.insert(newTask)
        }
    }

    fun getTaskById(id: Int): Flow<Task?> {
        return repository.getTaskById(id)
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }

    fun onTaskChecked(task: Task, isChecked: Boolean) {
        if (isChecked) {
            viewModelScope.launch {
                // Lösche alten Task
                repository.delete(task)

                // Wenn Intervall gesetzt, neuen Task anlegen
                if (task.interval > 0) {
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    val oldDateTime = LocalDateTime.parse("${task.date} ${task.time}", formatter)
                    val newDateTime = oldDateTime.plusMinutes(task.interval.toLong())

                    val newTask = task.copy(
                        id = 0, // Neu ID vergeben
                        date = newDateTime.toLocalDate().toString(),
                        time = newDateTime.toLocalTime().toString(),
                        isDone = false
                    )
                    repository.insert(newTask)
                }
            }
        } else {
            // Wenn abgehakt zurückgenommen: evtl. nichts tun oder Task reaktivieren
        }
    }
}

data class TaskUiState(
    val taskList: List<Task> = emptyList()
)
