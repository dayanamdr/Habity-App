package com.example.habity.feature_habit.presentation.home_section

import com.example.habity.feature_habit.domain.model.Habit
import com.example.habity.feature_habit.domain.util.HabitOrder

sealed class HabitsEvent {
    data class Order(val habitOrder: HabitOrder): HabitsEvent()
    data class DeleteHabit(val habit: Habit): HabitsEvent()
    object RestoreHabit: HabitsEvent()
    object ToggleOrderSection: HabitsEvent()
}
