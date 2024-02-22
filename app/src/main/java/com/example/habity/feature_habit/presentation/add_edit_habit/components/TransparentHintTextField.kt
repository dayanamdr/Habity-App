package com.example.habity.feature_habit.presentation.add_edit_habit.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun TransparentHintTextField(
    text: String,
    hint: String,
    label: String,
    modifier: Modifier = Modifier,
    isHintVisible: Boolean = true,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    isNumberField: Boolean = false,
    onFocusChange: (FocusState) -> Unit
) {
    Column(
        modifier = Modifier
    ) {
        Text(label)
        Box(
            modifier = modifier,
            contentAlignment = Alignment.CenterStart
        ) {
            TextField(
                value = text,
                onValueChange = onValueChange,
                singleLine = singleLine,
                textStyle = textStyle,
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (isNumberField) KeyboardType.Number else KeyboardType.Text
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        onFocusChange(it)
                    }
            )

            if (isHintVisible) {
                Text(
                    text = hint,
                    modifier = Modifier.padding(start = 6.dp),
                    style = textStyle,
                    color = Color.DarkGray
                )
            }
        }
    }

}