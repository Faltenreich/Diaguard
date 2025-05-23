package com.faltenreich.diaguard.statistic.distribution

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.statistic.StatisticState

@Composable
fun StatisticDistributionChart(
    state: StatisticState.Distribution,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        drawCircle(
            color = Color.White,
            radius = size.height / 2,
            center = center,
        )
    }
}