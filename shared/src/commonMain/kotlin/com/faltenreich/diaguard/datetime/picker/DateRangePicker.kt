package com.faltenreich.diaguard.datetime.picker

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults.dateFormatter
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.cancel
import diaguard.shared.generated.resources.ok
import androidx.compose.material3.DateRangePicker as MaterialDateRangePicker

@Composable
fun DateRangePicker(
    dateRange: ClosedRange<Date>,
    // FIXME: Offsets by minus one day
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
        val dateFormatter = remember { dateFormatter() }
        MaterialDateRangePicker(
            state = state,
            // Workaround: Force align paddings to DatePickerDialog
            // see https://issuetracker.google.com/issues/325309575
            title = {
                DateRangePickerDefaults.DateRangePickerTitle(
                    displayMode = state.displayMode,
                    modifier = Modifier.padding(
                        PaddingValues(
                            start = 24.dp,
                            end = 12.dp,
                            top = 16.dp,
                        ),
                    ),
                )
            },
            headline = {
                DateRangePickerDefaults.DateRangePickerHeadline(
                    selectedStartDateMillis = state.selectedStartDateMillis,
                    selectedEndDateMillis = state.selectedEndDateMillis,
                    displayMode = state.displayMode,
                    dateFormatter = dateFormatter,
                    modifier = Modifier.padding(
                        PaddingValues(
                            start = 24.dp,
                            end = 12.dp,
                            bottom = 12.dp,
                        ),
                    ),
                )
            }
        )
    }
}