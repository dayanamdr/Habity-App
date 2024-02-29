package com.example.habity.feature_habit.domain.repository

import com.example.habity.feature_habit.domain.model.Habit
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {
    suspend fun getAll(): List<Habit>
    suspend fun getById(id: Int): Habit?
    suspend fun insertHabit(habit: Habit): Habit
    suspend fun deleteHabit(habit: Habit)
    suspend fun updateHabit(habit: Habit): Habit?
}
