package com.example.habity.feature_habit.domain.use_case

import android.util.Log
import com.example.habity.feature_habit.data.network.NetworkStatusChecker
import com.example.habity.feature_habit.domain.model.Habit
import com.example.habity.feature_habit.domain.model.InvalidHabitException
import com.example.habity.feature_habit.domain.repository.LocalRepository
import com.example.habity.feature_habit.domain.repository.RemoteRepository

class AddEditUseCase(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val networkStatusChecker: NetworkStatusChecker
) {
    @Throws(InvalidHabitException::class)
    suspend operator fun invoke(habit: Habit) {
        if (habit.name.isBlank()) {
            throw InvalidHabitException("The name of the item can't be empty.")
        }
        if (habit.label.isBlank()) {
            throw InvalidHabitException("The label of the item can't be empty.")
        }
        try {
            if (habit.id != null) { // Handle update
                Log.d("UpdateHabitUseCase", "updateHabit: $habit")
                if (networkStatusChecker.isCurrentlyAvailable()) {
                    val updatedHabit = remoteRepository.updateHabit(habit)

                    Log.d("UpdateHabitUseCase", "newUpdatedEntity: $updatedHabit")
                    localRepository.updateHabit(habit)
                } else {
                    Log.d("UpdateHabitUseCase", "Could not update. No internet connection.")
                    throw Exception("Could not update. No internet.")
                }
            } else { // Handle addition
                Log.d("AddHabitUseCase", "newHabit: $habit")
                if (networkStatusChecker.isCurrentlyAvailable()) {
                    val newHabitEntity = remoteRepository.insertHabit(habit)

                    Log.d("AddHabitUseCase", "newHabitEntity: $newHabitEntity")
                    Log.d("AddHabitUseCase", "newHabitEntityID: ${newHabitEntity.id}")

                    localRepository.insertHabit(habit.copy(id= newHabitEntity.id, action = null))
                } else {
                    Log.d("AddHabitUseCase", "newHabitEntity added locally: $habit")
                    localRepository.insertHabit(habit.copy(id= -1, action = "add"))
                }
            }
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }
}