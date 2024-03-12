package com.example.habity.feature_habit.data.repository

import android.util.Log
import com.example.habity.feature_habit.data.data_source.local.HabitDao
import com.example.habity.feature_habit.domain.model.Habit
import com.example.habity.feature_habit.domain.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

    override suspend fun getHabitsByDate(date: String): Flow<List<Habit>> {
        Log.d("LocalRepositoryImpl", "getHabitsByDate = $date")
        return dao.getHabitsByDate(date)
    }

    override fun getCompletedHabitsCountOfWeek(startOfWeek: String, endOfWeek: String): Flow<List<Int>> {
        Log.d("LocalRepositoryImpl", "getCompletedHabitsCountOfWeek")
        Log.d("LocalRepositoryImpl", "startOfWeek=$startOfWeek, endOfWeek=$endOfWeek")
        return dao.getCompletedHabitsOfWeek(startOfWeek, endOfWeek)
            .map { habits ->
                val habitsCountList = MutableList(7) { 0 }
                habits.forEach {habit ->
                    val dayOfWeek = getDayOfWeekIndex(habit.date)
                    habitsCountList[dayOfWeek] += 1
                }
                habitsCountList
            }
    }

    private fun getDayOfWeekIndex(dateString: String): Int {
        // Parse the date string to get the day of the week index
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.time = date
        var dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY // Adjust for Monday as 0
        if (dayOfWeek < 0) dayOfWeek += 7 // Ensure the index is within range (0 to 6)
        return dayOfWeek
    }

    override fun getCompletedHabitsOfWeek(
        startOfWeek: String,
        endOfWeek: String
    ): Flow<Map<String, Int>> {
        Log.d("LocalRepositoryImpl", "getCompletedHabitsOfWeek")
        Log.d("LocalRepositoryImpl", "startOfWeek=$startOfWeek, endOfWeek=$endOfWeek")
        return dao.getCompletedHabitsOfWeek(startOfWeek, endOfWeek)
            .map { habits ->
                val habitsCountMap = mutableMapOf<String, Int>()
                // Initialize count for each day to 0
                habits.forEach { habit ->
                    habitsCountMap[habit.date] = 0
                }
                // Count completed habits for each day
                habits.forEach { habit ->
                    habitsCountMap[habit.date] = habitsCountMap[habit.date]?.plus(1) ?: 1
                }
                habitsCountMap
            }
    }
}