package com.example.habity.feature_habit.domain.use_case

import android.util.Log
import com.example.habity.feature_habit.data.network.NetworkStatusChecker
import com.example.habity.feature_habit.domain.model.Habit
import com.example.habity.feature_habit.domain.repository.LocalRepository
import com.example.habity.feature_habit.domain.repository.RemoteRepository

class ToggleCompleteHabitUseCase(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val networkStatusChecker: NetworkStatusChecker
) {
    suspend operator fun invoke(habit: Habit) {
        Log.d("ToggleCompleteHabitUseCase", "habit: $habit")
        if (networkStatusChecker.isCurrentlyAvailable()) {
            val updatedHabit = remoteRepository.updateHabit(habit)

            Log.d("ToggleCompleteHabitUseCase", "newUpdatedEntity: $updatedHabit")
            localRepository.updateHabit(habit)
        } else {
            localRepository.updateHabit(habit.copy(action = "update"))
            Log.d("ToggleCompleteHabitUseCase",
                "Updated complete status locally. No internet connection.")
        }
    }
}
