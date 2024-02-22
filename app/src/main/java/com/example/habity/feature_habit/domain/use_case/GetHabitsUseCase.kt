package com.example.habity.feature_habit.domain.use_case

import com.example.habity.feature_habit.domain.model.Habit
import com.example.habity.feature_habit.domain.repository.LocalRepository
import com.example.habity.feature_habit.domain.util.HabitOrder
import com.example.habity.feature_habit.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetHabitsUseCase(
    private val localRepository: LocalRepository
) {
    operator fun invoke(
        habitOrder: HabitOrder = HabitOrder.Date(OrderType.Descending)): Flow<List<Habit>> {
        return localRepository.getAllHabits().map { habits ->
            when(habitOrder.orderType) {
                is OrderType.Ascending -> {
                    when (habitOrder) {
                        is HabitOrder.Name -> {habits.sortedBy { it.name.lowercase() }}
                        is HabitOrder.Label -> {habits.sortedBy { it.label.lowercase() }}
                        is HabitOrder.Date -> {habits.sortedBy { it.date }}
                    }
                }
                is OrderType.Descending -> {
                    when (habitOrder) {
                        is HabitOrder.Name -> {habits.sortedByDescending { it.name.lowercase() }}
                        is HabitOrder.Label -> {habits.sortedByDescending { it.label.lowercase() }}
                        is HabitOrder.Date -> {habits.sortedByDescending { it.date }}
                    }
                }
            }
        }
    }
}