package com.faltenreich.diaguard.datetime.picker

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.cancel
import diaguard.shared.generated.resources.ok

@Composable
fun TimePickerDialog(
    time: Time,
    onDismissRequest: () -> Unit,
    onConfirmRequest: (Time) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberTimePickerState(
        initialHour = time.hourOfDay,
        initialMinute = time.minuteOfHour,
    )
    TimePickerPlatformDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    val update = time.copy(
                        hourOfDay = state.hour,
                        minuteOfHour = state.minute,
                    )
                    onConfirmRequest(update)
                },
            ) {
                Text(getString(Res.string.ok))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(getString(Res.string.cancel))
            }
        },
    ) {
        TimePicker(state = state)
    }
}