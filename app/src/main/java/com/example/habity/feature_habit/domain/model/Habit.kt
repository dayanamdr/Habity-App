package com.example.habity.feature_habit.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val idLocal: Int? = null,
    val id: Int?,
    val name: String,
    val description: String,
    val label: String,
    val date: String,
    val completed: Boolean,
    var action: String?
)

class InvalidHabitException(message: String): Exception(message)

