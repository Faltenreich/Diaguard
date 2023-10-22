package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
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
        initialSelectedEndDateMillis = dateRange.endInclusive.atStartOfDay().millisSince1970,
        initialDisplayMode = DisplayMode.Picker,
    )
    Column {
        DatePickerDialog(
            onDismissRequest = { onPick(dateRange) },
            // Workaround: Buttons are cut off, so we set them manually
            // FIXME: Wrong height when switching to DisplayMode.Input
            confirmButton = {},
            dismissButton = {}
        ) {
            MaterialDateRangePicker(
                state = state,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = AppTheme.dimensions.padding.P_3),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = { onPick(dateRange) }) {
                    Text(getString(MR.strings.cancel))
                }
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
                    enabled = state.selectedEndDateMillis != null,
                ) {
                    Text(getString(MR.strings.ok))
                }
            }
        }
    }
}