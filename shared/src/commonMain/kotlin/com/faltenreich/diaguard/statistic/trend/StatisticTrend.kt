package com.faltenreich.diaguard.statistic.trend

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.view.preview.AppPreview
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
private fun Preview() = AppPreview {
    StatisticTrend(
        state = StatisticTrendState.preview(this),
    )
}