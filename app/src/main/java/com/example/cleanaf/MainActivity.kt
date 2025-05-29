package com.example.cleanaf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cleanaf.data.Task
import com.example.cleanaf.ui.screens.AddTaskScreen
import com.example.cleanaf.ui.screens.TaskDetailScreen
import com.example.cleanaf.ui.screens.TaskListScreen
import com.example.cleanaf.ui.theme.CleanAFTheme
import com.example.cleanaf.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CleanAFTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val taskViewModel: TaskViewModel = hiltViewModel()

                    NavHost(
                        navController = navController,
                        startDestination = "taskList"
                    ) {
                        composable(route = "taskList") {
                            TaskListScreen(
                                navController = navController,
                                viewModel = taskViewModel,      // ViewModel übergeben
                                onTaskClick = { taskId ->
                                    navController.navigate("taskDetail/$taskId")
                                }
                            )
                        }
                        composable(
                            route = "taskDetail/{taskId}",
                            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
                        ) {
                            val taskId = it.arguments?.getInt("taskId") ?: return@composable
                            TaskDetailScreen(
                                taskId = taskId,
                                navController = navController,
                                viewModel = taskViewModel
                            )
                        }
                        composable(route = "addTask") {
                            AddTaskScreen(
                                navController = navController,
                                viewModel = taskViewModel,
                            )
                        }
                    }

                }
            }
        }
    }
}
