package com.example.cleanaf.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanaf.data.Task
import com.example.cleanaf.data.TaskRepository
import com.example.cleanaf.util.PointsManager
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

    fun getAllTasks(): Flow<List<Task>> = repository.getAllTasks()

    fun update(task: Task, context: Context, onPointsChanged: () -> Unit) {
        viewModelScope.launch {
            val updatedTask = if (task.isDone && !task.pointsAwarded) {
                PointsManager.addPoints(context, task.points)
                onPointsChanged()
                task.copy(pointsAwarded = true)
            } else {
                task
            }
            repository.update(updatedTask)
        }
    }



    fun insertTask(
        name: String,
        description: String,
        date: String,
        time: String,
        interval: Int,
        difficulty: String
    ) {
        viewModelScope.launch {
            val newTask = Task(
                name = name,
                description = description,
                date = date,
                time = time,
                interval = interval,
                points = pointsForDifficulty(difficulty),
                isDone = false,
                pointsAwarded = false,
                difficulty = difficulty
            )

            repository.insert(newTask)
        }
    }

    fun pointsForDifficulty(difficulty: String): Int {
        return when (difficulty) {
            "easy" -> 10
            "medium" -> 25
            "hard" -> 50
            else -> 0
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
                repository.delete(task)

                if (task.interval > 0) {
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    var newDateTime = LocalDateTime.parse("${task.date} ${task.time}", formatter)
                    val now = LocalDateTime.now()

                    do {
                        newDateTime = newDateTime.plusMinutes(task.interval.toLong())
                    } while (newDateTime <= now)


                    val newTask = task.copy(
                        id = 0,
                        date = newDateTime.toLocalDate().toString(),
                        time = newDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                        isDone = false
                    )

                    Log.d("TaskDebug", "New task created for: ${newTask.date} ${newTask.time}")

                    repository.insert(newTask)
                }
            }
        }
    }

}

data class TaskUiState(
    val taskList: List<Task> = emptyList()
)
