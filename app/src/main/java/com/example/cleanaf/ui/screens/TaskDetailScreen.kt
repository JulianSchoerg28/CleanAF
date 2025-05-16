package com.example.cleanaf.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cleanaf.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: Int,
    navController: NavController,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val task = viewModel.getTaskById(taskId).collectAsState(initial = null).value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        }
    ) { padding ->
        if (task != null) {
            Column(modifier = Modifier
                .padding(padding)
                .padding(16.dp)) {
                Text("Titel: ${task.title}", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Text("Beschreibung: ${task.description}")
            }
        } else {
            Box(modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Task nicht gefunden.")
            }
        }
    }
}

