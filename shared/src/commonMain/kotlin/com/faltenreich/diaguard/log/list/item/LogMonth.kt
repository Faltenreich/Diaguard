package com.faltenreich.diaguard.log.list.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.shared.di.inject

private const val ASPECT_RATIO = 24f / 9f

@Composable
fun LogMonth(
    state: LogItemState.MonthHeader,
    modifier: Modifier = Modifier,
    formatter: DateTimeFormatter = inject(),
) {
    Box(
        modifier = modifier
            .aspectRatio(ASPECT_RATIO)
            .background(AppTheme.colors.scheme.primary)
            .padding(
                start = AppTheme.dimensions.padding.P_3,
                top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
                    + AppTheme.dimensions.padding.P_3,
                end = AppTheme.dimensions.padding.P_3,
                bottom = AppTheme.dimensions.padding.P_3,
            ),
        contentAlignment = Alignment.BottomStart,
    ) {
        Text(
            text = formatter.formatMonthOfYear(state.date.monthOfYear, abbreviated = false),
            color = AppTheme.colors.scheme.onPrimary,
            style = AppTheme.typography.headlineSmall,
        )
    }
}