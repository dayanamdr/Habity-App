package com.example.habity.feature_habit.data.data_source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.habity.feature_habit.domain.model.Habit
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE idLocal = :id")
    suspend fun getHabitById(id: Int): Habit?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("Delete FROM habits")
    suspend fun deleteAllHabitEntities()

    @Query("SELECT * FROM habits WHERE action IS NOT NULL")
    fun getHabitEntitiesWithPendingActions(): List<Habit>

    @Query("SELECT * FROM habits WHERE name = :name AND label = :label LIMIT 1")
    suspend fun findHabitByAttributes(name: String, label: String): Habit?
}