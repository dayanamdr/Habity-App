package com.example.habity.feature_habit.domain.use_case

import com.example.habity.feature_habit.data.network.NetworkStatusChecker
import com.example.habity.feature_habit.domain.repository.LocalRepository
import com.example.habity.feature_habit.domain.repository.RemoteRepository

class GetHabitsByDateUseCase(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val networkStatusChecker: NetworkStatusChecker
) {
}
