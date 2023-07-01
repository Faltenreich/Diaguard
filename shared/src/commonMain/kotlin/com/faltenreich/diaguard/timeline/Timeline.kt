package com.faltenreich.diaguard.timeline

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.platform.LocalDensity
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.timeline.chart.TimelineChart
import com.faltenreich.diaguard.timeline.chart.TimelineChartConfig
import com.faltenreich.diaguard.timeline.list.TimelineList

@Composable
fun Timeline(
    modifier: Modifier = Modifier,
    viewModel: TimelineViewModel = inject(),
) {
    val viewState = viewModel.viewState.collectAsState().value
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        TimelineChart(
            initialDate = viewState.initialDate,
            values = viewState.bloodSugarList,
            config = TimelineChartConfig(
                padding = LocalDensity.current.run { AppTheme.dimensions.padding.P_2.toPx() },
                fontPaint = Paint().apply { color = AppTheme.colors.material.onBackground },
                fontSize = LocalDensity.current.run { AppTheme.typography.bodyMedium.fontSize.toPx() },
                gridStrokeColor = AppTheme.colors.material.onSurfaceVariant,
                valueColorNormal = AppTheme.colors.Green,
                valueColorLow = AppTheme.colors.Blue,
                valueColorHigh = AppTheme.colors.Red,
            ),
            onDateChange = viewModel::setDate,
            modifier = Modifier.weight(2f),
        )
        TimelineList(
            modifier = Modifier.weight(1f),
        )
    }
}