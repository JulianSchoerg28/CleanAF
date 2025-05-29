package com.example.cleanaf.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cleanaf.viewmodel.TaskViewModel
import java.time.format.DateTimeFormatter

@Composable
fun EditTaskScreen(
    taskId: Int,
    navController: NavController,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val task by viewModel.getTaskById(taskId).collectAsState(initial = null)

    task?.let {
        var title by remember { mutableStateOf(it.name) }
        var description by remember { mutableStateOf(it.description) }
        var date by remember { mutableStateOf(it.date) }
        var time by remember { mutableStateOf(it.time) }
        var intervalText by remember { mutableStateOf(it.interval.toString()) }
        var difficulty by remember {
            mutableStateOf(
                when (it.points) {
                    10 -> "easy"
                    25 -> "medium"
                    50 -> "hard"
                    else -> "easy"
                }
            )
        }

        val isInputValid = remember(title, date, time) {
            title.isNotBlank() && isValidDate(date) && isValidTime(time)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date (YYYY-MM-DD)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                isError = date.isNotEmpty() && !isValidDate(date)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text("Time (HH:mm)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                isError = time.isNotEmpty() && !isValidTime(time)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = intervalText,
                onValueChange = { intervalText = it.filter { char -> char.isDigit() } },
                label = { Text("Interval (minutes, optional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Difficulty")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                listOf("easy", "medium", "hard").forEach { diff ->
                    Button(
                        onClick = { difficulty = diff },
                        colors = if (difficulty == diff) ButtonDefaults.buttonColors() else ButtonDefaults.outlinedButtonColors()
                    ) {
                        Text(diff.replaceFirstChar { it.uppercase() })
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val interval = intervalText.toIntOrNull() ?: 0
                    val updatedTask = it.copy(
                        name = title.trim(),
                        description = description.trim(),
                        date = date.trim(),
                        time = time.trim(),
                        interval = interval,
                        points = viewModel.pointsForDifficulty(difficulty)
                    )
                    viewModel.update(updatedTask)
                    navController.popBackStack()
                },
                enabled = isInputValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }
        }
    }
}
