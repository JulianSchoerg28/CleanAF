package com.example.cleanaf.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cleanaf.viewmodel.TaskViewModel
import com.example.cleanaf.data.Task
import com.example.cleanaf.ui.components.TaskItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: Int,
    navController: NavController,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val task by viewModel.getTaskById(taskId).collectAsState(initial = null)
    var showDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Task Details") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("taskList") {
                            popUpTo(0) { inclusive = true }
                        }                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("editTask/${task?.id}")
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Task")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Task")
                    }
                }
            )
        }
    ) { padding ->
        task?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(text = it.name, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Description: ${it.description}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Date: ${it.date}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Time: ${it.time}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Interval: ${it.interval}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Points: ${it.points}")
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Task") },
            text = { Text("Are you sure you want to delete this task?") },
            confirmButton = {
                TextButton(onClick = {
                    task?.let { viewModel.deleteTask(it, context) }
                    showDeleteDialog = false
                    navController.navigate("taskList") {
                        popUpTo("taskList") { inclusive = true }
                    }
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
