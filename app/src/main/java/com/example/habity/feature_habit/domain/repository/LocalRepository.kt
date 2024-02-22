package com.example.habity.feature_habit.domain.repository

import com.example.habity.feature_habit.domain.model.Habit
import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    fun getAllHabits(): Flow<List<Habit>>

    suspend fun getHabitById(id: Int): Habit?

    suspend fun insertHabit(habit: Habit)

    suspend fun deleteHabit(habit: Habit)
}