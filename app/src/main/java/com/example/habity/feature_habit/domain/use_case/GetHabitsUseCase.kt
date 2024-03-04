package com.example.habity.feature_habit.domain.use_case

import android.util.Log
import com.example.habity.feature_habit.data.network.NetworkStatusChecker
import com.example.habity.feature_habit.domain.model.Habit
import com.example.habity.feature_habit.domain.repository.LocalRepository
import com.example.habity.feature_habit.domain.repository.RemoteRepository
import com.example.habity.feature_habit.domain.util.HabitOrder
import com.example.habity.feature_habit.domain.util.OrderType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetHabitsUseCase(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val networkStatusChecker: NetworkStatusChecker
) {
    operator fun invoke(
        habitOrder: HabitOrder = HabitOrder.Date(OrderType.Descending),
        forceUpdateHabitsState: Boolean = false
    ): Flow<List<Habit>> = flow {
        val isNetworkAvailable = networkStatusChecker.isCurrentlyAvailable()
        val localEntities = localRepository.getAllHabits().first()
        Log.d("GetHabitsUseCase", "Local entities: $localEntities")

        if (localEntities.isEmpty() || forceUpdateHabitsState) {
            if (isNetworkAvailable) {
                try {
                    val remoteEntities = remoteRepository.getAll()
                    Log.d("GetHabitsUseCase", "Fetched remote entities: $remoteEntities")
                    localRepository.clearAndCacheHabitEntities(flowOf(remoteEntities))
                    Log.d("GetHabitsUseCase", "Cleared and cached")
                    emitAll(flowOf(remoteEntities))
                } catch (e: Exception) {
                    Log.e("GetHabitsUseCase", "Error fetching from remote, falling back to local cache")
                    emitAll(flowOf(localEntities))
                }
            } else {
                emitAll(flowOf(localEntities))
            }
        } else {
            emitAll(flowOf(localEntities))
        }
    }.flowOn(Dispatchers.IO)

    //    operator fun invoke(
//        habitOrder: HabitOrder = HabitOrder.Date(OrderType.Descending)): Flow<List<Habit>> {
//        return localRepository.getAllHabits().map { habits ->
//            when(habitOrder.orderType) {
//                is OrderType.Ascending -> {
//                    when (habitOrder) {
//                        is HabitOrder.Name -> {habits.sortedBy { it.name.lowercase() }}
//                        is HabitOrder.Label -> {habits.sortedBy { it.label.lowercase() }}
//                        is HabitOrder.Date -> {habits.sortedBy { it.date }}
//                    }
//                }
//                is OrderType.Descending -> {
//                    when (habitOrder) {
//                        is HabitOrder.Name -> {habits.sortedByDescending { it.name.lowercase() }}
//                        is HabitOrder.Label -> {habits.sortedByDescending { it.label.lowercase() }}
//                        is HabitOrder.Date -> {habits.sortedByDescending { it.date }}
//                    }
//                }
//            }
//        }
//    }
}