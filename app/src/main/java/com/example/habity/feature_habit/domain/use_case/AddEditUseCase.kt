package com.example.habity.feature_habit.domain.use_case

import android.util.Log
import com.example.habity.feature_habit.domain.model.Habit
import com.example.habity.feature_habit.domain.model.InvalidHabitException
import com.example.habity.feature_habit.domain.repository.LocalRepository

class AddEditUseCase(
    private val localRepository: LocalRepository
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
            localRepository.insertHabit(habit)
            Log.d("AddHabitUseCase", "newHabit: $habit")
//            if (networkChecker.isNetworkAvailable()) {
//                val newItem = serverRepo.insertItem(item)
//
//                Log.d("AddItemUseCase", "newItem: $newItem")
//                Log.d("AddItemUseCase", "newItemID: ${newItem.id}")
//                //repository.insertItem(item.copy(id= newItem.id, action = null))
//                repository.insertItem(item.copy(id= newItem.id, action = null))
//            } else {
//                repository.insertItem(item.copy(id = -1, action = "add"))
//            }
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }
}