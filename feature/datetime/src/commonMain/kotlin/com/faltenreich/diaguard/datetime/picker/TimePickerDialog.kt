package com.faltenreich.diaguard.datetime.picker

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.datetime.Time
import diaguard.feature.datetime.generated.resources.Res
import diaguard.feature.datetime.generated.resources.cancel
import diaguard.feature.datetime.generated.resources.ok
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

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
                Text(stringResource(Res.string.ok))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(Res.string.cancel))
            }
        },
    ) {
        TimePicker(state = state)
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    TimePickerDialog(
        time = now().time,
        onDismissRequest = {},
        onConfirmRequest = {},
    )
}