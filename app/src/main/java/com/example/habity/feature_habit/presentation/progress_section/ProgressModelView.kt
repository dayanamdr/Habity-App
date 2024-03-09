package com.example.habity.feature_habit.presentation.progress_section

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habity.feature_habit.data.network.NetworkStatusChecker
import com.example.habity.feature_habit.domain.repository.LocalRepository
import com.example.habity.feature_habit.domain.repository.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val networkStatusChecker: NetworkStatusChecker,
): ViewModel() {
    private val _state = mutableStateOf(ProgressHabitsState())
    val state: State<ProgressHabitsState> = _state

    private val _isNetworkAvailable = MutableStateFlow(false)
    val isNetworkAvailable: MutableStateFlow<Boolean> = _isNetworkAvailable

    init {
        val currentDate = LocalDate.now()
        viewModelScope.launch {
            localRepository.getHabitsByDate(currentDate.toString()).collect {dailyHabits ->
                // print("daily habits =  ${dailyHabits}\n")
                _state.value = _state.value.copy(dailyHabits = dailyHabits)
            }
        }
    }

}
