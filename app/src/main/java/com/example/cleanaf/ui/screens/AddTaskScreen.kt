package com.example.cleanaf.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cleanaf.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun AddTaskScreen(
    navController: NavController,
    viewModel: TaskViewModel
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var time by remember { mutableStateOf(LocalTime.now().withSecond(0).withNano(0).toString()) }
    var interval by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("easy") }

    val difficultyOptions = listOf("easy", "medium", "hard")

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Add New Task", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
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
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = time,
            onValueChange = { time = it },
            label = { Text("Time (HH:MM)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = interval,
            onValueChange = { interval = it },
            label = { Text("Interval (e.g. daily, weekly)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text("Difficulty:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            difficultyOptions.forEach { option ->
                FilterChip(
                    selected = difficulty == option,
                    onClick = { difficulty = option },
                    label = { Text(option) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val points = when (difficulty) {
                    "easy" -> 10
                    "medium" -> 25
                    "hard" -> 50
                    else -> 10
                }
                viewModel.insertTask(
                    title,
                    description,
                    date,
                    time,
                    interval,
                    points
                )
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}
