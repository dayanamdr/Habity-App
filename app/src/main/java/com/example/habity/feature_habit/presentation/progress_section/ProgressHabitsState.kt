package com.example.habity.feature_habit.presentation.progress_section

import com.example.habity.feature_habit.domain.model.Habit

data class ProgressHabitsState(
    val dailyHabits: List<Habit> = emptyList(),
    val weeklyHabits: List<Habit> = emptyList()
)
