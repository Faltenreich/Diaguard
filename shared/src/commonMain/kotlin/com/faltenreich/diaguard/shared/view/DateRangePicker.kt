package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import androidx.compose.material3.DateRangePicker as MaterialDateRangePicker

@Composable
fun DateRangePicker(
    dateRange: ClosedRange<Date>,
    onPick: (ClosedRange<Date>) -> Unit,
    dateTimeFactory: DateTimeFactory = inject(),
) {
    val state = rememberDateRangePickerState(
        initialSelectedStartDateMillis = dateRange.start.atStartOfDay().millisSince1970,
    )
    DatePickerDialog(
        onDismissRequest = { onPick(dateRange) },
        confirmButton = {
            TextButton(
                onClick = {
                    val start = state.selectedStartDateMillis?.let(dateTimeFactory::dateTime)?.date ?: dateRange.start
                    val end = state.selectedEndDateMillis?.let(dateTimeFactory::dateTime)?.date ?: dateRange.endInclusive
                    onPick(start .. end)
                },
            ) {
                Text(getString(MR.strings.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onPick(dateRange) }) {
                Text(getString(MR.strings.cancel))
            }
        }
    ) {
        MaterialDateRangePicker(state = state)
    }
}