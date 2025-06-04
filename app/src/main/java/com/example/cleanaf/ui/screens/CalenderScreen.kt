package com.example.cleanaf.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cleanaf.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navController: NavController,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val tasks by viewModel.getAllTasks().collectAsState(initial = emptyList())
    val today = LocalDate.now()
    val upcomingTasks = tasks.filter {
        runCatching { LocalDate.parse(it.date) >= today }.getOrDefault(false)
    }
    val grouped = upcomingTasks.groupBy { it.date }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calender Overview") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            grouped.toSortedMap().forEach { (date, tasksOnDate) ->
                item {
                    Text(
                        text = formatDate(date),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 8.dp)
                    )
                }
                items(tasksOnDate) { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("editTask/${task.id}") }
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(task.name, style = MaterialTheme.typography.bodyLarge)
                            if (task.description.isNotBlank()) {
                                Text(task.description, style = MaterialTheme.typography.bodyMedium)
                            }
                            Text(task.time, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

private fun formatDate(dateStr: String): String {
    return try {
        val parsed = LocalDate.parse(dateStr)
        parsed.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    } catch (e: Exception) {
        dateStr
    }
}
