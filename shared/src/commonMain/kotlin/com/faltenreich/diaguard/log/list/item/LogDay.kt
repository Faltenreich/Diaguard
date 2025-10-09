package com.faltenreich.diaguard.log.list.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LogDay(
    state: LogDayState,
    modifier: Modifier = Modifier,
) = with(state) {
    if (style.isVisible) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_1),
        ) {
            Text(
                text = dayOfMonthLocalized,
                color =
                    if (style.isHighlighted) AppTheme.colors.scheme.primary
                    else AppTheme.colors.scheme.onBackground,
                fontWeight = if (style.isHighlighted) FontWeight.Bold else FontWeight.Normal,
                style = AppTheme.typography.headlineSmall,
            )
            Text(
                text = dayOfWeekLocalized,
                style = AppTheme.typography.labelMedium,
            )
        }
    } else {
        Box(modifier = modifier)
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    LogDay(
        state = LogDayState(
            date = today(),
            dayOfMonthLocalized = "01",
            dayOfWeekLocalized = "Mon",
            style = LogDayStyle(
                isVisible = true,
                isHighlighted = true,
            )
        ),
    )
}