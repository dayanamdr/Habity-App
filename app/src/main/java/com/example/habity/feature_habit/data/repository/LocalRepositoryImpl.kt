package com.example.habity.feature_habit.data.repository

import android.util.Log
import com.example.habity.feature_habit.data.data_source.HabitDao
import com.example.habity.feature_habit.domain.model.Habit
import com.example.habity.feature_habit.domain.repository.LocalRepository
import kotlinx.coroutines.flow.Flow

class LocalRepositoryImpl(
    private val dao: HabitDao
): LocalRepository {
    override fun getAllHabits(): Flow<List<Habit>> {
        return dao.getAllHabits()
    }

    override suspend fun getHabitById(id: Int): Habit? {
        return dao.getHabitById(id)
    }

    override suspend fun insertHabit(habit: Habit) {
        Log.d("LocalRepositoryImpl", "Insert habit with id ${habit.id}")
        return dao.insertHabit(habit)
    }

    override suspend fun deleteHabit(habit: Habit) {
        Log.d("LocalRepositoryImpl", "Delete habit with id ${habit.id}")
        return dao.deleteHabit(habit)
    }

}