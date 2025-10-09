package com.faltenreich.diaguard.log.list.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LogMonth(
    state: LogItemState.MonthHeader,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(AppTheme.colors.scheme.primary)
            .padding(AppTheme.dimensions.padding.P_3)
            .fillMaxWidth()
            .height(AppTheme.dimensions.size.LogMonthHeight),
        contentAlignment = Alignment.BottomStart,
    ) {
        Text(
            text = state.dateLocalized,
            color = AppTheme.colors.scheme.onPrimary,
            style = AppTheme.typography.headlineSmall,
        )
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    val date = today()
    LogMonth(
        state = LogItemState.MonthHeader(
            dayState = LogDayState(
                date = date,
                dayOfMonthLocalized = "01",
                dayOfWeekLocalized = "Mon",
                style = LogDayStyle(
                    isVisible = false,
                    isHighlighted = false,
                ),
            ),
            dateLocalized = date.toString(),
        ),
    )
}