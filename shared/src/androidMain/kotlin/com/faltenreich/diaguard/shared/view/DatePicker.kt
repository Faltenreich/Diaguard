package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.DateTimeApi
import com.faltenreich.diaguard.shared.di.inject
import dev.icerock.moko.resources.compose.stringResource

@Composable
actual fun DatePicker(
    dateTime: DateTime,
    onDatePick: (DateTime) -> Unit,
) {
    val dateTimeApi = inject<DateTimeApi>()
    val datePickerState = rememberDatePickerState()
    androidx.compose.material3.DatePickerDialog(
        onDismissRequest = { onDatePick(dateTime) },
        confirmButton = {
            TextButton(
                onClick = {
                    val millis = datePickerState.selectedDateMillis ?: return@TextButton
                    val localDateTime = dateTimeApi.millisToDateTime(millis)
                    onDatePick(localDateTime)
                }
            ) {
                Text(stringResource(MR.strings.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDatePick(dateTime) }) {
                Text(stringResource(MR.strings.cancel))
            }
        },
    ) {
        androidx.compose.material3.DatePicker(state = datePickerState)
    }
}