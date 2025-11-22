package com.faltenreich.diaguard.datetime.picker

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults.dateFormatter
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.injection.inject
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.cancel
import diaguard.shared.generated.resources.ok
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DateRangePickerDialog(
    dateRange: DateRange,
    onDismissRequest: () -> Unit,
    onConfirmRequest: (DateRange) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberDateRangePickerState(
        initialSelectedStartDateMillis = dateRange.start.atStartOfDay().millisSince1970,
        initialSelectedEndDateMillis = dateRange.endInclusive.atStartOfDay().millisSince1970,
        initialDisplayMode = DisplayMode.Picker,
    )
    androidx.compose.material3.DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    val dateTimeFactory = inject<DateTimeFactory>()
                    val start =
                        state.selectedStartDateMillis?.let(dateTimeFactory::dateTimeFromEpoch)?.date
                    val end =
                        state.selectedEndDateMillis?.let(dateTimeFactory::dateTimeFromEpoch)?.date
                    if (start != null && end != null) {
                        onConfirmRequest(start..end)
                    } else {
                        onDismissRequest()
                    }
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
        val dateFormatter = remember { dateFormatter() }
        DateRangePicker(
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

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    DateRangePickerDialog(
        dateRange = today().let { today -> DateRange(today, today) },
        onDismissRequest = {},
        onConfirmRequest = {},
    )
}