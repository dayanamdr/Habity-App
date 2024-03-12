package com.example.habity.feature_habit.presentation.progress_section

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habity.feature_habit.data.network.NetworkStatusChecker
import com.example.habity.feature_habit.domain.repository.LocalRepository
import com.example.habity.feature_habit.domain.repository.RemoteRepository
import com.example.habity.feature_habit.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val networkStatusChecker: NetworkStatusChecker,
    private val habitUseCases: UseCases
): ViewModel() {
    private val _state = mutableStateOf(ProgressHabitsState())
    val state: State<ProgressHabitsState> = _state

    private val _isNetworkAvailable = MutableStateFlow(false)
    val isNetworkAvailable: MutableStateFlow<Boolean> = _isNetworkAvailable

    init {
        initDailyHabitsProgress()
        initWeeklyHabitsProgress()
    }

    private fun initDailyHabitsProgress() {
        viewModelScope.launch {
            val currentDate = LocalDate.now()
            localRepository.getHabitsByDate(currentDate.toString()).collect {dailyHabits ->
                _state.value = _state.value.copy(dailyHabits = dailyHabits)
            }
        }
    }

    private fun initWeeklyHabitsProgress() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now()
        val startOfWeek = today.with(DayOfWeek.MONDAY).format(formatter)
        val endOfWeek = today.with(DayOfWeek.SUNDAY).format(formatter)
        loadCompletedHabitsCountOfWeek(startOfWeek = startOfWeek, endOfWeek=endOfWeek)
    }

    private fun loadCompletedHabitsCountOfWeek(startOfWeek: String, endOfWeek: String) {
        viewModelScope.launch {
            habitUseCases.getWeeklyHabitsUseCase(startOfWeek, endOfWeek).collect { weeklyCompletedHabitsCount ->
                _state.value = _state.value.copy(weeklyCompletedHabits = weeklyCompletedHabitsCount)
            }
        }
    }
}
