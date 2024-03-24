package com.faltenreich.diaguard.datetime

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.TimePickerDialog

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
                Text(getString(MR.strings.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onPick(time) }) {
                Text(getString(MR.strings.cancel))
            }
        },
    ) {
        androidx.compose.material3.TimePicker(state = state)
    }
}