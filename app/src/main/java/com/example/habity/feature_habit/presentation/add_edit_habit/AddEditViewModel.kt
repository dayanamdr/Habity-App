package com.example.habity.feature_habit.presentation.add_edit_habit

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habity.feature_habit.domain.model.Habit
import com.example.habity.feature_habit.domain.use_case.UseCases
import com.example.habity.feature_habit.presentation.home_section.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditMealViewModel @Inject constructor(
    private val habitUseCases: UseCases,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val _habitName = mutableStateOf(HabitTextFieldState(
        hint = "Enter name...",
    ))
    val habitName: State<HabitTextFieldState> = _habitName

    private val _habitDescription = mutableStateOf(HabitTextFieldState(
        hint = "Enter description..."
    ))
    val habitDescription: State<HabitTextFieldState> = _habitDescription

    private val _habitLabel = mutableStateOf(HabitTextFieldState(
        hint = "Enter label (e.g. gym, water, sleep, running)"
    ))
    val habitLabel: State<HabitTextFieldState> = _habitLabel

    private val _habitDate = mutableStateOf(HabitTextFieldState(
        hint = "Enter date (e.g. 1-02-2024)"
    ))
    val habitDate: State<HabitTextFieldState> = _habitDate

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var currentHabitId: Int? = null
    var currentHabitLocalId: Int? = null

    init {
        Log.i("AddEditHabitViewModel", "started")
        Log.i("AddEditHabitViewModel", "id: ${savedStateHandle.get<Int>("habitId")}")

        savedStateHandle.get<Int>("habitId")?.let {habitId ->
            if (habitId != -1) {
                Log.i("AddEditHabitViewModel", "habitId: $habitId")
                viewModelScope.launch {
                    print("habit id = ${habitId}")
                    habitUseCases.getHabitUseCase(habitId)?.also { habit ->
                        print("habit = ${habit}\n")
                        currentHabitId = habit.id
                        currentHabitLocalId = habit.idLocal
                        _habitName.value = habitName.value.copy(
                            text = habit.name,
                            isHintVisible = false
                        )
                        _habitDescription.value = habitDescription.value.copy(
                            text = habit.description,
                            isHintVisible = false
                        )
                        _habitLabel.value = habitLabel.value.copy(
                            text = habit.label,
                            isHintVisible = false
                        )
                        _habitDate.value = habitDate.value.copy(
                            text = habit.date,
                            isHintVisible = false
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditHabitEvent) {
        when(event) {
            is AddEditHabitEvent.EnteredName -> {
                _habitName.value = habitName.value.copy(
                    text = event.value
                )
            }
            is AddEditHabitEvent.ChangeNameFocus -> {
                _habitName.value = habitName.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            habitName.value.text.isBlank()
                )
            }
            is AddEditHabitEvent.EnteredDescription -> {
                _habitDescription.value = habitDescription.value.copy(
                    text = event.value,
                )
            }
            is AddEditHabitEvent.ChangeDescriptionFocus -> {
                _habitDescription.value = habitDescription.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            habitDescription.value.text.isBlank()
                )
            }
            is AddEditHabitEvent.EnteredLabel -> {
                _habitLabel.value = habitLabel.value.copy(
                    text = event.value,
                )
            }
            is AddEditHabitEvent.ChangeLabelFocus -> {
                _habitLabel.value = habitLabel.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            habitLabel.value.text.isBlank()
                )
            }
            is AddEditHabitEvent.EnteredDate -> {
                _habitDate.value = habitDate.value.copy(
                    text = event.value,
                )
            }
            is AddEditHabitEvent.ChangeDateFocus -> {
                _habitDate.value = habitDate.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            habitDate.value.text.isBlank()
                )
            }
            is AddEditHabitEvent.SaveHabit -> {
                viewModelScope.launch {
                    try {
                        if (currentHabitId == null && currentHabitLocalId == null) {
                            val habitToAdd = Habit(
                                name = habitName.value.text,
                                label = habitLabel.value.text,
                                description = habitDescription.value.text,
                                date = habitDate.value.text,
                                id = currentHabitId,
                                idLocal = currentHabitLocalId,
                                completed = false,
                                action = null
                            )
                            habitUseCases.addEditUseCase(habitToAdd)
                        } else {
                            val habitToUpdate = Habit(
                                name = habitName.value.text,
                                label = habitLabel.value.text,
                                description = habitDescription.value.text,
                                date = habitDate.value.text,
                                id = currentHabitId,
                                idLocal = currentHabitLocalId,
                                completed = false,
                                action = null
                            )
                            val result = habitUseCases.addEditUseCase(habitToUpdate)
                        }
                        _eventFlow.emit(UiEvent.SaveHabit)
                    } catch(e: Exception) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Could not save habit."
                            )
                        )
                    }
                }
            }
            is AddEditHabitEvent.CancelSaveHabit -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.CancelSaveHabit)
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveHabit: UiEvent()
        object CancelSaveHabit: UiEvent()
    }
}