package com.example.habity.feature_habit.domain.use_case

import android.util.Log
import com.example.habity.feature_habit.data.network.NetworkStatusChecker
import com.example.habity.feature_habit.domain.model.Habit
import com.example.habity.feature_habit.domain.repository.LocalRepository
import com.example.habity.feature_habit.domain.repository.RemoteRepository

class GetHabitUseCase(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val networkStatusChecker: NetworkStatusChecker
) {
    suspend operator fun invoke(id: Int): Habit? {
        val habitEntity = localRepository.getHabitById(id)

        Log.d("GetHabitUseCase", "retrieved HabitEntity: $habitEntity")

        return habitEntity
    }
}