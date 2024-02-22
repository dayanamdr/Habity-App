package com.example.habity.feature_habit.presentation.add_edit_habit

import androidx.compose.ui.focus.FocusState

sealed class AddEditHabitEvent {
    data class EnteredName(val value: String): AddEditHabitEvent()
    data class ChangeNameFocus(val focusState: FocusState): AddEditHabitEvent()
    data class EnteredDescription(val value: String): AddEditHabitEvent()
    data class ChangeDescriptionFocus(val focusState: FocusState): AddEditHabitEvent()
    data class EnteredLabel(val value: String): AddEditHabitEvent()
    data class ChangeLabelFocus(val focusState: FocusState): AddEditHabitEvent()
    data class EnteredDate(val value: String): AddEditHabitEvent()
    data class ChangeDateFocus(val focusState: FocusState): AddEditHabitEvent()
    object SaveHabit: AddEditHabitEvent()
    object CancelSaveHabit: AddEditHabitEvent()
}

