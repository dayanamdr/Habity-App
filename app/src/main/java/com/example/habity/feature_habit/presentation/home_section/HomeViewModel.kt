package com.example.habity.feature_habit.presentation.home_section

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habity.feature_habit.domain.model.Habit
import com.example.habity.feature_habit.domain.use_case.UseCases
import com.example.habity.feature_habit.domain.util.HabitOrder
import com.example.habity.feature_habit.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val habitUseCases: UseCases,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _state = mutableStateOf(HabitsState())
    val state: State<HabitsState> = _state

    private var recentlyDeletedHabit: Habit? = null
    private var getHabitsJob: Job? = null

    init {
        viewModelScope.launch {
            getHabits(HabitOrder.Date(OrderType.Descending))
        }

    }

    fun onEvent(event: HabitsEvent) {
        when(event) {
            is HabitsEvent.Order -> {
                if (state.value.habitOrder::class == event.habitOrder::class &&
                    state.value.habitOrder.orderType == event.habitOrder.orderType
                ) {
                    return
                }
                getHabits(event.habitOrder)
            }
            is HabitsEvent.DeleteHabit -> {
                viewModelScope.launch {
                    try {
                        habitUseCases.deleteHabitUseCase(event.habit)
                        recentlyDeletedHabit = event.habit
                    } catch(e: Exception) {
                        Log.d("delete HomeViewModel", "Could not delete habit.")
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Could not delete habit."
                            )
                        )
                    }
                }
            }
            is HabitsEvent.RestoreHabit -> {
                viewModelScope.launch {
                    try {
                        habitUseCases.addEditUseCase(recentlyDeletedHabit?: return@launch)
                        recentlyDeletedHabit = null
                    } catch(e: Exception) {
                        Log.d("restoreMeal HomeViewModel", "Could not restore habit.")
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Could not restore habit."
                            )
                        )
                    }
                }
            }
            is HabitsEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getHabits(habitOrder: HabitOrder) {
        getHabitsJob?.cancel()
        getHabitsJob = habitUseCases.getHabitsUseCase(habitOrder)
            .onEach { habits ->
                _state.value = state.value.copy(habits = habits,
                    habitOrder = habitOrder)
            }
            .launchIn(viewModelScope)
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
    }

//    override fun onCleared() {
//        webSocketManager.closeWebSocket()
//        super.onCleared()
//    }
}