package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.datetime.DateTimeApi
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.di.inject
import dev.icerock.moko.resources.compose.stringResource

@Composable
actual fun TimePicker(
    time: Time,
    onPick: (Time) -> Unit,
) {
    val api = inject<DateTimeApi>()
    val state = rememberTimePickerState(
        initialHour = time.hourOfDay,
        initialMinute = time.minuteOfHour,
        is24Hour = true,
    )
    // TODO: Replace with TimePickerDialog
    DatePickerDialog(
        onDismissRequest = { onPick(time) },
        confirmButton = {
            TextButton(onClick = { onPick(api.time(state.hour, state.minute)) }) {
                Text(stringResource(MR.strings.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onPick(time) }) {
                Text(stringResource(MR.strings.cancel))
            }
        },
    ) {
        androidx.compose.material3.TimePicker(state = state)
    }
}