package com.faltenreich.diaguard.log.list.item

import androidx.compose.foundation.layout.height
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LogMonth(
    state: LogItemState.MonthHeader,
    modifier: Modifier = Modifier,
    formatter: DateTimeFormatter = inject(),
) {
    CenterAlignedTopAppBar(
        title = { Text(formatter.formatMonthOfYear(state.date.monthOfYear, abbreviated = false)) },
        modifier = modifier.height(AppTheme.dimensions.size.LogMonthHeight),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppTheme.colors.scheme.primary,
            navigationIconContentColor = AppTheme.colors.scheme.onPrimary,
            titleContentColor = AppTheme.colors.scheme.onPrimary,
            actionIconContentColor = AppTheme.colors.scheme.onPrimary,
        ),
    )
}

@Preview
@Composable
private fun Preview() = AppPreview {
    LogMonth(
        state = LogItemState.MonthHeader(
            date = today(),
        ),
    )
}