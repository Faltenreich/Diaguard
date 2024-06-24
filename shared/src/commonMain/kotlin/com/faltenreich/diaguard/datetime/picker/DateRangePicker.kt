package com.faltenreich.diaguard.datetime.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import org.jetbrains.compose.resources.painterResource
import androidx.compose.material3.DateRangePicker as MaterialDateRangePicker

@Composable
fun DateRangePicker(
    dateRange: ClosedRange<Date>,
    onPick: (ClosedRange<Date>) -> Unit,
    modifier: Modifier = Modifier,
    dateTimeFactory: DateTimeFactory = inject(),
) {
    val state = rememberDateRangePickerState(
        initialSelectedStartDateMillis = dateRange.start.atStartOfDay().millisSince1970,
        initialSelectedEndDateMillis = dateRange.endInclusive.atStartOfDay().millisSince1970,
        initialDisplayMode = DisplayMode.Picker,
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.scheme.surface),
        verticalArrangement = Arrangement.Top,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimensions.padding.P_2_5),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { onPick(dateRange) }) {
                Icon(
                    painter = painterResource(Res.drawable.ic_clear),
                    contentDescription = getString(Res.string.close),
                )
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
                Text(getString(Res.string.save))
            }
        }
        MaterialDateRangePicker(
            state = state,
            modifier = Modifier.padding(top = AppTheme.dimensions.padding.P_3),
        )
    }
}