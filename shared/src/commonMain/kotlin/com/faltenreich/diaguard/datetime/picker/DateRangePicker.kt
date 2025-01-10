package com.faltenreich.diaguard.datetime.picker

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.*
import androidx.compose.material3.DateRangePicker as MaterialDateRangePicker

@Composable
fun DateRangePicker(
    dateRange: ClosedRange<Date>,
    onPick: (ClosedRange<Date>) -> Unit,
    dateTimeFactory: DateTimeFactory = inject(),
) {
    val state = rememberDateRangePickerState(
        initialSelectedStartDateMillis = dateRange.start.atStartOfDay().millisSince1970,
        initialSelectedEndDateMillis = dateRange.endInclusive.atStartOfDay().millisSince1970,
        initialDisplayMode = DisplayMode.Picker,
    )
    DatePickerDialog(
        onDismissRequest = { onPick(dateRange) },
        confirmButton = {
            TextButton(
                onClick = {
                    val start = state.selectedStartDateMillis
                        ?.let(dateTimeFactory::dateTime)?.date
                        ?: dateRange.start
                    val end = state.selectedEndDateMillis
                        ?.let(dateTimeFactory::dateTime)?.date
                        ?: dateRange.endInclusive
                    onPick(start..end)
                },
            ) {
                Text(getString(Res.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onPick(dateRange) }) {
                Text(getString(Res.string.cancel))
            }
        },
    ) {
        MaterialDateRangePicker(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                // FIXME: Padding is off
                //  see https://issuetracker.google.com/issues/325309575
                .padding(top = AppTheme.dimensions.padding.P_3),
        )
    }
}