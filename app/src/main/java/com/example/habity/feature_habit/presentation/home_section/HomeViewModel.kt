package com.example.habity.feature_habit.presentation.home_section

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habity.feature_habit.data.network.NetworkStatusChecker
import com.example.habity.feature_habit.data.network.WebSocketClient
import com.example.habity.feature_habit.domain.model.Habit
import com.example.habity.feature_habit.domain.repository.LocalRepository
import com.example.habity.feature_habit.domain.repository.RemoteRepository
import com.example.habity.feature_habit.domain.use_case.UseCases
import com.example.habity.feature_habit.domain.util.HabitOrder
import com.example.habity.feature_habit.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val habitUseCases: UseCases,
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val networkStatusChecker: NetworkStatusChecker,
    private val webSocketClient: WebSocketClient
): ViewModel() {
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _state = mutableStateOf(HabitsState())
    val state: State<HabitsState> = _state

    private val _isNetworkAvailable = MutableStateFlow(false)
    val isNetworkAvailable: MutableStateFlow<Boolean> = _isNetworkAvailable

    private val _newHabitEntity = mutableStateOf<Habit?>(null)
    val newHabitEntity: State<Habit?> = _newHabitEntity

    private val _showNewHabitEntityDialog = mutableStateOf(false)
    val showNewHabitEntityDialog: State<Boolean> = _showNewHabitEntityDialog

    private var recentlyDeletedHabit: Habit? = null
    private var getHabitsJob: Job? = null

    init {
        viewModelScope.launch {
            //getHabits(HabitOrder.Date(OrderType.Descending))
            localRepository.getAllHabits().collect { habits ->
                val updatedEntitiesList = habits.filterNot {it.action == "delete"}

                print("habits init = $habits\n")
                _state.value = _state.value.copy(habits = updatedEntitiesList)
            }
        }
        observeNetworkStatus()
        observeWebSocketEvents()
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkStatusChecker.networkStatus.collect { status ->
                _isNetworkAvailable.value = status == NetworkStatusChecker.NetworkStatus.Available
                if (_isNetworkAvailable.value) {
                    Log.d("HomeViewModel", "Network is available, syncing pending changes.")
                    syncPendingChanges()
                }
            }
        }
    }

    private suspend fun syncPendingChanges() {
        viewModelScope.launch(Dispatchers.IO) {
            val pendingHabitEntities = localRepository.getHabitsWithPendingActions()
            pendingHabitEntities.forEach { habitEntity ->
                when (habitEntity.action) {
                    "add" -> {
                        val newEntity = remoteRepository.insertHabit(habitEntity)
                        localRepository.updateHabit(habitEntity.copy(id = newEntity.id, action = null))
                    }
                    "delete" -> {
                        remoteRepository.deleteHabit(habitEntity)
                        localRepository.deleteHabit(habitEntity)
                    }
                    "update" -> {
                        remoteRepository.updateHabit(habitEntity)
                        localRepository.updateHabit(habitEntity.copy(action = null))
                    }
                }
            }
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
                //getHabits(event.habitOrder)
            }
            is HabitsEvent.DeleteHabit -> {
                viewModelScope.launch {
                    try {
                        val result = habitUseCases.deleteHabitUseCase(event.habit)
                        if (result == "success") {
                            recentlyDeletedHabit = event.habit
                            Log.d("delete HomeViewModel", "Deleted successfully.")
                        } else {
                            Log.d("delete HomeViewModel", "Deleted locally. No internet connection")
                        }
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

    private fun observeWebSocketEvents() {
        viewModelScope.launch {
            try {
                webSocketClient.connect("ws://10.0.2.2:2419/ws")
                webSocketClient.events.collect { event ->
                    val newHabitEntity = Habit(
                        idLocal = 0,
                        id = event.id,
                        name = event.name,
                        description = event.description,
                        label = event.label,
                        date = event.date,
                        completed = event.completed,
                        action = null
                    )
                    _newHabitEntity.value = newHabitEntity

                    Log.i("HomeViewModel", "newHabitEntity: $newHabitEntity")

                    val foundHabitEntity = localRepository.findHabitByAttributes(
                        newHabitEntity.name,
                        newHabitEntity.label
                    )

                    Log.d("HomeViewModel", "habit entity found: $foundHabitEntity")
                    if(foundHabitEntity == null) {
                        addHabitEntityAndUpdateState(newHabitEntity)
                        displayNewHabitEntityNotification()
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error observing WebSocket events:", e)
                // Decide if reconnection is needed or if you want to signal an error state
            }
        }
    }

    private suspend fun addHabitEntityAndUpdateState(habitEntity: Habit) {
        habitUseCases.addEditUseCase(habitEntity)
        // Update state
        val updatedHabitsList = habitUseCases.getHabitsUseCase().first()
        Log.d("HomeViewModel", "updatedEntities: $updatedHabitsList")
        //_state.value = _state.value.copy(habits = updatedHabitsList)
    }

    private fun displayNewHabitEntityNotification() {
        _showNewHabitEntityDialog.value = true
    }

    fun dismissNewHabitEntityDialog() {
        _showNewHabitEntityDialog.value = false
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
    }

//    override fun onCleared() {
//        webSocketManager.closeWebSocket()
//        super.onCleared()
//    }
}