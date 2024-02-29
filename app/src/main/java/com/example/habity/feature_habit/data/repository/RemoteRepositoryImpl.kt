package com.example.habity.feature_habit.data.repository

import android.util.Log
import com.example.habity.feature_habit.data.data_source.remote.ApiService
import com.example.habity.feature_habit.domain.model.Habit
import com.example.habity.feature_habit.domain.repository.RemoteRepository

class RemoteRepositoryImpl(
    private val apiService: ApiService,
): RemoteRepository {
    override suspend fun getAll(): List<Habit> {
        return try {
            val response = apiService.retrieveAllHabits()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getById(id: Int): Habit? {
        try {
            val response = apiService.retrieveHabit(id)
            if (response.isSuccessful) {
                return response.body() ?: throw Exception("Failed to retrieve habit entity")
            } else {
                throw Exception("Failed to retrieve habit entity: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("RemoteRepository", "Error getting habit entity: ${e.message}")
            throw Exception(e.message.toString())
        }
    }

    override suspend fun insertHabit(habit: Habit): Habit {
        try {
            val response = apiService.createHabit(habit)
            if (response.isSuccessful) {
                return response.body() ?: throw Exception("Failed to retrieve created habit entity")
            } else {
                throw Exception("Failed to create habit entity: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("RemoteRepository", "Error inserting habit entity: ${e.message}")
            throw Exception(e.message.toString())
        }
    }

    override suspend fun deleteHabit(habit: Habit) {
        try {
            val response = apiService.deleteHabit(habit.id)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Failed to retrieve deleted habit entity")
            } else {
                throw Exception("Failed to delete habit entity: ${response.errorBody()?.string()}")
            }
        } catch(e: Exception) {
            Log.e("RemoteRepository", "Error deleting habit entity: ${e.message}")
            throw Exception(e.message)
        }
    }

    override suspend fun updateHabit(habit: Habit): Habit? {
        try {
            val response = apiService.updateHabit(habit.id!!, habit)
            if (response.isSuccessful) {
                return response.body() ?: throw Exception("Failed to retrieve updated habit entity")
            } else {
                throw Exception("Failed to update habit entity: ${response.errorBody()?.string()}")
            }
        } catch(e: Exception) {
            Log.e("RemoteRepository", "Error updating habit entity: ${e.message}")
            throw Exception(e.message)
        }
    }
}
