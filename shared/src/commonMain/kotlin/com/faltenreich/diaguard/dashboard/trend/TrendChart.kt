package com.faltenreich.diaguard.dashboard.trend

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.dashboard.DashboardState

@Composable
fun TrendChart(
    state: DashboardState.Trend,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier.fillMaxSize(),
    ) {
        state.days.forEachIndexed { index, date ->

        }
    }
}