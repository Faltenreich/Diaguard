package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTimeApi
import com.faltenreich.diaguard.shared.di.inject
import dev.icerock.moko.resources.compose.stringResource

@Composable
actual fun DatePicker(
    date: Date,
    onPick: (Date) -> Unit,
) {
    val dateTimeApi = inject<DateTimeApi>()
    val datePickerState = rememberDatePickerState()
    androidx.compose.material3.DatePickerDialog(
        onDismissRequest = { onPick(date) },
        confirmButton = {
            TextButton(
                onClick = {
                    val millis = datePickerState.selectedDateMillis ?: return@TextButton
                    val dateTime = dateTimeApi.millisToDateTime(millis)
                    onPick(dateTime.date)
                }
            ) {
                Text(stringResource(MR.strings.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onPick(date) }) {
                Text(stringResource(MR.strings.cancel))
            }
        },
    ) {
        androidx.compose.material3.DatePicker(state = datePickerState)
    }
}