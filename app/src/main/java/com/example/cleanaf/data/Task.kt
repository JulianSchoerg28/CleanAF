package com.example.cleanaf.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val isDone: Boolean = false,
    val date: String,
    val time: String,
    val interval: String,
    val points: Int
)
