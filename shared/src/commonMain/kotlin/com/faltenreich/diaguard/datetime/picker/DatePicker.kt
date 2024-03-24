package com.faltenreich.diaguard.datetime.picker

import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import androidx.compose.material3.DatePicker as MaterialDatePicker

@Composable
fun DatePicker(
    date: Date,
    onPick: (Date) -> Unit,
    dateTimeFactory: DateTimeFactory = inject(),
) {
    val dateTime = date.atStartOfDay()
    val state = rememberDatePickerState(initialSelectedDateMillis = dateTime.epochMilliseconds)
    DatePickerDialog(
        onDismissRequest = { onPick(date) },
        confirmButton = {
            TextButton(
                onClick = {
                    onPick(state.selectedDateMillis?.let(dateTimeFactory::dateTime)?.date ?: date)
                },
            ) {
                Text(getString(MR.strings.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onPick(date) }) {
                Text(getString(MR.strings.cancel))
            }
        },
    ) {
        MaterialDatePicker(state = state)
    }
}