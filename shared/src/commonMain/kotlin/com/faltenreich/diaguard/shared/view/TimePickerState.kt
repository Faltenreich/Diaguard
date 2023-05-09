package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class TimePickerState {

    var isShown by mutableStateOf(false)

    companion object {

        val Saver: Saver<TimePickerState, *> = Saver(
            save = { it.isShown },
            restore = {
                TimePickerState().apply { isShown = it }
            }
        )
    }
}

@Composable
fun rememberTimePickerState(): TimePickerState {
    return rememberSaveable(saver = TimePickerState.Saver) {
        TimePickerState()
    }
}