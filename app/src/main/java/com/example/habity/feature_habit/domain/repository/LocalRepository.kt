package com.example.habity.feature_habit.domain.repository

import com.example.habity.feature_habit.domain.model.Habit
import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    fun getAllHabits(): Flow<List<Habit>>

    suspend fun getHabitById(id: Int): Habit?

    suspend fun insertHabit(habit: Habit)
    suspend fun updateHabit(habit: Habit)

    suspend fun deleteHabit(habit: Habit)

    suspend fun getHabitsWithPendingActions(): List<Habit>

    suspend fun clearAndCacheHabitEntities(habitsFlow: Flow<List<Habit>>)

    suspend fun findHabitByAttributes(name: String, label: String): Habit?

    suspend fun getHabitsByDate(date: String): Flow<List<Habit>>
}