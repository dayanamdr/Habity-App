package com.example.habity.feature_habit.presentation.progress_section

import androidx.lifecycle.MutableLiveData
import com.example.habity.feature_habit.domain.model.Habit

data class ProgressHabitsState(
    val dailyHabits: List<Habit> = emptyList(),
    val weeklyHabits: Map<String, Int> = emptyMap(),
    val weeklyCompletedHabits: List<Int> = emptyList()
) {
}
