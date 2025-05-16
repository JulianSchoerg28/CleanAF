package com.example.cleanaf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cleanaf.ui.model.Task
import com.example.cleanaf.ui.screens.TaskDetailScreen
import com.example.cleanaf.ui.screens.TaskListScreen
import com.example.cleanaf.ui.theme.CleanAFTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CleanAFTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val tasks = listOf(
                        Task(1, "Müll rausbringen", "Bitte Bio- und Restmüll entsorgen"),
                        Task(2, "Küche putzen", "Arbeitsfläche, Herd und Spüle reinigen")
                    )
                    NavHost(
                        navController = navController,
                        startDestination = "taskList"
                    ) {
                        composable("taskList") {
                            TaskListScreen(
                                tasks = tasks,
                                onTaskClick = {
                                    navController.navigate("taskDetail")
                                }
                            )
                        }
                        composable("taskDetail") {
                            TaskDetailScreen(onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}
