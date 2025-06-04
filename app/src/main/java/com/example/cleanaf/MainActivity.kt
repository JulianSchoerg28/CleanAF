package com.example.cleanaf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cleanaf.datastore.SettingsDataStore
import com.example.cleanaf.ui.screens.AddTaskScreen
import com.example.cleanaf.ui.screens.CalendarScreen
import com.example.cleanaf.ui.screens.EditTaskScreen
import com.example.cleanaf.ui.screens.PresetTaskScreen
import com.example.cleanaf.ui.screens.RewardScreen
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

            val context = LocalContext.current
            val darkModeFlow = remember { SettingsDataStore.readDarkMode(context) }
            val isDarkMode by darkModeFlow.collectAsState(initial = false)

            CleanAFTheme(darkTheme = isDarkMode) {
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
                                viewModel = taskViewModel,
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
                        composable(
                            route = "addTask?title={title}&desc={desc}&interval={interval}&difficulty={difficulty}",
                            arguments = listOf(
                                navArgument("title") { defaultValue = ""; nullable = true },
                                navArgument("desc") { defaultValue = ""; nullable = true },
                                navArgument("interval") { defaultValue = -1 },
                                navArgument("difficulty") { defaultValue = "easy" }
                            )
                        ) {
                            val args = it.arguments!!
                            AddTaskScreen(
                                navController = navController,
                                viewModel = taskViewModel,
                                presetTitle = args.getString("title"),
                                presetDescription = args.getString("desc"),
                                presetInterval = args.getInt("interval").takeIf { it >= 0 },
                                presetDifficulty = args.getString("difficulty")
                            )
                        }

                        composable("editTask/{taskId}") { backStackEntry ->
                            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull() ?: return@composable
                            EditTaskScreen(taskId = taskId, navController = navController)
                        }
                        composable("presetTasks") {
                            PresetTaskScreen(navController = navController)
                        }
                        composable("calendar") {
                            CalendarScreen(navController = navController)
                        }
                        composable("rewards") {
                            RewardScreen(navController = navController)
                        }




                    }

                }
            }
        }
    }
}
