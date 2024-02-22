package com.example.habity.feature_habit.domain.use_case

import com.example.habity.feature_habit.domain.model.Habit
import com.example.habity.feature_habit.domain.repository.LocalRepository

class DeleteHabitUseCase(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(habit: Habit) {
        localRepository.deleteHabit(habit)
    }
}