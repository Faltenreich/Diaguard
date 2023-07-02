package com.faltenreich.diaguard.timeline

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.timeline.chart.TimelineChart

@Composable
fun Timeline(
    modifier: Modifier = Modifier,
    viewModel: TimelineViewModel = inject(),
) {
    val viewState = viewModel.viewState.collectAsState().value
    TimelineChart(
        initialDate = viewState.initialDate,
        currentDate = viewState.currentDate,
        valuesForChart = viewState.valuesForChart,
        propertiesForList = viewState.propertiesForList,
        onDateChange = viewModel::setDate,
        modifier = modifier.fillMaxSize(),
    )
}