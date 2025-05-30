package com.example.cleanaf.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cleanaf.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    navController: NavController,
    viewModel: TaskViewModel,
    presetTitle: String? = null,
    presetDescription: String? = null,
    presetInterval: Int? = null,
    presetDifficulty: String? = null
) {
    var title by remember { mutableStateOf(presetTitle ?: "") }
    var description by remember { mutableStateOf(presetDescription ?: "") }
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var time by remember { mutableStateOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))) }
    var intervalText by remember { mutableStateOf(presetInterval?.toString() ?: "") }
    var difficulty by remember { mutableStateOf(presetDifficulty ?: "easy") }

    val isInputValid = remember(title, date, time) {
        title.isNotBlank() &&
                isValidDate(date) &&
                isValidTime(time)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Task") },
                navigationIcon = {
                    IconButton(onClick = {navController.navigate("taskList") {
                        popUpTo("taskList") { inclusive = true }
                    }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                },
                actions = {
                    TextButton(onClick = { navController.navigate("presetTasks") }) {
                        Text("Presets")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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
                    viewModel.insertTask(
                        title.trim(),
                        description.trim(),
                        date.trim(),
                        time.trim(),
                        interval,
                        difficulty,
                    )
                    navController.navigate("taskList") {
                        popUpTo("taskList") { inclusive = true }
                    }
                },
                enabled = isInputValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}

fun isValidDate(date: String): Boolean {
    return try {
        LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
        true
    } catch (e: Exception) {
        false
    }
}

fun isValidTime(time: String): Boolean {
    return try {
        LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
        true
    } catch (e: Exception) {
        false
    }
}
