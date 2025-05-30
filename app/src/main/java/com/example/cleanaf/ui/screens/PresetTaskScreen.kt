package com.example.cleanaf.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cleanaf.viewmodel.TaskViewModel

// Datenklasse für Presets
data class PresetTask(
    val name: String,
    val description: String,
    val interval: Int,
    val difficulty: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresetTaskScreen(
    navController: NavController,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val presets = listOf(
        PresetTask("Müll rausbringen", "Restmüll, Biomüll, Plastik", 1440, "easy"),
        PresetTask("Staubsaugen", "Wohnung saugen", 10080, "medium"),
        PresetTask("Bad putzen", "Waschbecken, Dusche, Klo", 10080, "hard")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Presets") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(presets) { preset ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate(
                            "addTask?title=${preset.name}&desc=${preset.description}&interval=${preset.interval}&difficulty=${preset.difficulty}"
                        )
                    }
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = preset.name, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = preset.description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
