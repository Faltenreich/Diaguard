package com.faltenreich.diaguard.datetime.picker

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.cancel
import diaguard.shared.generated.resources.ok
import androidx.compose.material3.TimePicker as MaterialTimePicker

@Composable
fun TimePicker(
    time: Time,
    onPick: (Time) -> Unit,
) {
    val state = rememberTimePickerState(
        initialHour = time.hourOfDay,
        initialMinute = time.minuteOfHour,
        is24Hour = true,
    )
    TimePickerDialog(
        onDismissRequest = { onPick(time) },
        confirmButton = {
            TextButton(onClick = { onPick(time.copy(hourOfDay = state.hour, minuteOfHour = state.minute)) }) {
                Text(getString(Res.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onPick(time) }) {
                Text(getString(Res.string.cancel))
            }
        },
    ) {
        MaterialTimePicker(state = state)
    }
}