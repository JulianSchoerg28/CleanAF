package com.example.cleanaf.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cleanaf.ui.components.TaskItem
import com.example.cleanaf.viewmodel.TaskViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.cleanaf.datastore.SettingsDataStore
import kotlinx.coroutines.launch
import java.time.LocalDate
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import com.example.cleanaf.util.PointsManager




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    navController: NavHostController,
    viewModel: TaskViewModel = hiltViewModel(),
    onTaskClick: (Int) -> Unit
) {
    var showAll by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    val today = LocalDate.now().toString()
    val context = LocalContext.current
    val totalPoints = remember { mutableStateOf(PointsManager.getPoints(context)) }
    val scope = rememberCoroutineScope()
    val darkModeFlow = remember { SettingsDataStore.readDarkMode(context) }
    val isDarkMode by darkModeFlow.collectAsState(initial = false)

    val tasks by viewModel.getAllTasks().collectAsState(initial = emptyList())
    val filteredTasks = tasks
        .filter { if (showAll) true else it.date == today }
        .filter { it.name.contains(searchText, ignoreCase = true) }
    val snackbarHostState = remember { SnackbarHostState() }




    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Tasks") },
                actions = {
                    TextButton(onClick = { showAll = !showAll }) {
                        Text(if (showAll) "All" else "Today")
                    }
                    IconButton(onClick = {
                        scope.launch {
                            SettingsDataStore.saveDarkMode(context, !isDarkMode)
                        }
                    }) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                            contentDescription = if (isDarkMode) "light mode" else "dark mode"
                        )
                    }
                    IconButton(onClick = {
                        navController.navigate("calendar")
                    }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Kalender")
                    }

                }

            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("addTask") }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
            }
        },

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search for taskname") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Text(
                    text = "🏅 Punkte: ${totalPoints.value}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(start = 16.dp, bottom = 8.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredTasks) { task ->
                    TaskItem(
                        task = task,
                        onTaskClick = { onTaskClick(task.id) },
                        onCheckedChange = { checked ->
                            viewModel.update(task.copy(isDone = checked), context) {
                                totalPoints.value = PointsManager.getPoints(context)
                                if (checked) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = getRandomMotivation(),
                                            actionLabel = null,
                                            withDismissAction = false,
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

fun getRandomMotivation(): String {
    val messages = listOf(
        "Gut gemacht! 💪",
        "Wieder was erledigt! ✅",
        "Du bist unstoppable 🚀",
        "To-do? To-done. 🎯",
        "Saubere Arbeit! 🧼",
        "Loser"
    )
    return messages.random()
}
