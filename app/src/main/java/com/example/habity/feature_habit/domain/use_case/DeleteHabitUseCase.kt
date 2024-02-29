package com.example.habity.feature_habit.domain.use_case

import android.util.Log
import com.example.habity.feature_habit.data.network.NetworkStatusChecker
import com.example.habity.feature_habit.domain.model.Habit
import com.example.habity.feature_habit.domain.repository.LocalRepository
import com.example.habity.feature_habit.domain.repository.RemoteRepository

class DeleteHabitUseCase(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val networkStatusChecker: NetworkStatusChecker
) {
    suspend operator fun invoke(habit: Habit): String {
        return try {
            if (networkStatusChecker.isCurrentlyAvailable()) {
                val deleteEntity = remoteRepository.deleteHabit(habit)
                Log.d("DeleteItemUseCase", "deletedEntity: $deleteEntity")
                localRepository.deleteHabit(habit)
                "success"
            } else {
                localRepository.updateHabit(habit.copy(action="delete"))
                Log.d("DeleteItemUseCase", "Deletion failed. No internet connection.")
                "failure"
            }
        } catch (e: Exception) {
            Log.d("DeleteItemUseCase", "Could not found item.")
            "failure"
        }
    }
}