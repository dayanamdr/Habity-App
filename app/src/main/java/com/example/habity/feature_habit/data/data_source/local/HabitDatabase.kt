package com.example.habity.feature_habit.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.habity.feature_habit.data.data_source.local.HabitDao
import com.example.habity.feature_habit.domain.model.Habit

@Database(
    entities = [Habit::class],
    version = 1,
    exportSchema = false
)
abstract class HabitDatabase: RoomDatabase() {

    abstract val habitDao: HabitDao

    companion object {
        const val DATABASE_NAME = "habits_db"
    }
}