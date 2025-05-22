package com.example.cleanaf.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cleanaf.viewmodel.TaskViewModel

@Composable
fun TaskDetailScreen(
    taskId: Int,
    viewModel: TaskViewModel,
    navController: NavController
) {
    val task = viewModel.getTaskById(taskId).collectAsState(initial = null).value

    task?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(text = it.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Description: ${it.description}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Date: ${it.date}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Time: ${it.time}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Interval: ${it.interval}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Points: ${it.points}")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Back")
            }
        }
    } ?: run {
        Text("Task not found.", modifier = Modifier.padding(16.dp))
    }
}
