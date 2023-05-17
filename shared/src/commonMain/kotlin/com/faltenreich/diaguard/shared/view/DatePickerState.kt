package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class DatePickerState {

    var isShown by mutableStateOf(false)

    companion object {

        val Saver: Saver<DatePickerState, *> = Saver(
            save = { it.isShown },
            restore = {
                DatePickerState().apply { isShown = it }
            }
        )
    }
}

@Composable
fun rememberDatePickerState(): DatePickerState {
    return rememberSaveable(saver = DatePickerState.Saver) {
        DatePickerState()
    }
}