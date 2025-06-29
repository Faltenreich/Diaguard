package com.faltenreich.diaguard.log.list.item

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun LogMonth(
    state: LogItemState.MonthHeader,
    modifier: Modifier = Modifier,
    formatter: DateTimeFormatter = inject(),
) {
    CenterAlignedTopAppBar(
        title = { Text(formatter.formatMonthOfYear(state.date.monthOfYear, abbreviated = false)) },
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppTheme.colors.scheme.primary,
            navigationIconContentColor = AppTheme.colors.scheme.onPrimary,
            titleContentColor = AppTheme.colors.scheme.onPrimary,
            actionIconContentColor = AppTheme.colors.scheme.onPrimary,
        ),
    )
}