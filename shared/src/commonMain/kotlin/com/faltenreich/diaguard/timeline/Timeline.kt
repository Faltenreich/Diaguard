package com.faltenreich.diaguard.timeline

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.timeline.chart.TimelineChart
import com.faltenreich.diaguard.timeline.list.TimelineList

@Composable
fun Timeline(
    modifier: Modifier = Modifier,
    viewModel: TimelineViewModel = inject(),
) {
    val viewState = viewModel.viewState.collectAsState().value
    val values = when (viewState) {
        is TimelineViewState.Requesting -> emptyList()
        is TimelineViewState.Responding -> viewState.bloodSugarList
    }
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        TimelineChart(
            initialDate = viewState.date,
            values = values,
            modifier = Modifier.weight(2f),
            onDateChange = viewModel::setDate,
        )
        TimelineList(
            modifier = Modifier.weight(1f),
        )
    }
}