package com.faltenreich.diaguard.statistic.trend

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.theme.AppTheme
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.statistic.trend.StatisticTrendState.Interval
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StatisticTrend(
    state: StatisticTrendState?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(AppTheme.dimensions.size.StatisticTrendHeight)
            .padding(AppTheme.dimensions.padding.P_3),
    ) {
        state ?: return
        StatisticTrendChart(
            state = state,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    StatisticTrend(
        state = StatisticTrendState(
            intervals = week().map { date ->
                Interval(
                    dateRange = date .. date,
                    label = date.dayOfWeek.localized(),
                    average = null,
                )
            },
            targetValue = 120.0,
            maximumValue = 200.0,
        ),
    )
}