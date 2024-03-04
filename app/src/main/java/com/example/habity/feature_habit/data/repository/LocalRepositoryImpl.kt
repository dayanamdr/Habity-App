package com.example.habity.feature_habit.data.repository

import android.util.Log
import com.example.habity.feature_habit.data.data_source.local.HabitDao
import com.example.habity.feature_habit.domain.model.Habit
import com.example.habity.feature_habit.domain.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

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

    override suspend fun updateHabit(habit: Habit) {
        Log.d("LocalRepositoryImpl", "Update habit with id ${habit.id}")
        return dao.updateHabit(habit)
    }

    override suspend fun deleteHabit(habit: Habit) {
        Log.d("LocalRepositoryImpl", "Delete habit with id ${habit.id}")
        return dao.deleteHabit(habit)
    }

    override suspend fun getHabitsWithPendingActions(): List<Habit> {
        return dao.getHabitEntitiesWithPendingActions()
    }

    override suspend fun clearAndCacheHabitEntities(habitsFlow: Flow<List<Habit>>) {
        Log.d("LocalRepositoryImpl", "clear and cache habit entities")
        dao.deleteAllHabitEntities()

        habitsFlow.first().forEach { entity ->
            Log.d("LocalRepositoryImpl", "habit entity: $entity")
            insertHabit(entity)
            Log.i("LocalRepositoryImpl", "habit entity inserted")
        }

        val entitiesFromLocalDb = dao.getAllHabits()
        Log.d("LocalRepositoryImpl", "habit entities from db: $entitiesFromLocalDb")
    }

    override suspend fun findHabitByAttributes(name: String, label: String): Habit? {
        return dao.findHabitByAttributes(name, label)
    }
}