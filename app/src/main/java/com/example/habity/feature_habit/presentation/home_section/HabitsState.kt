package com.example.habity.feature_habit.presentation.home_section

import com.example.habity.feature_habit.domain.model.Habit
import com.example.habity.feature_habit.domain.util.HabitOrder
import com.example.habity.feature_habit.domain.util.OrderType

data class HabitsState(
    val habits: List<Habit> = emptyList(),
    val habitOrder: HabitOrder = HabitOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
