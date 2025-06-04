package com.example.cleanaf.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cleanaf.util.PointsManager
import com.example.cleanaf.util.RewardManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardScreen(navController: NavController) {
    val context = LocalContext.current
    val totalPoints = PointsManager.getPoints(context)
    val rewards = RewardManager.getUnlockedRewards(totalPoints)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🏆 Rewards") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Star, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            item {
                Text(
                    text = "You have $totalPoints points!",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            items(rewards) { reward ->
                val bgColor = if (reward.unlocked) Color(0xFFD1C4E9) else Color(0xFFEEEEEE)
                val textColor = if (reward.unlocked) Color.Black else Color.Gray
                val accentColor = if (reward.unlocked) Color(0xFF4CAF50) else Color.Transparent

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = bgColor)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Reward Icon",
                            tint = textColor,
                            modifier = Modifier.size(36.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = reward.name,
                                color = textColor,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Unlocks at ${reward.threshold} points",
                                color = textColor
                            )
                            if (reward.unlocked) {
                                Text(
                                    text = "🎉 Unlocked!",
                                    color = accentColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
