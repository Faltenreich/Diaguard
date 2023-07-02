package com.faltenreich.diaguard.timeline

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun Timeline(
    modifier: Modifier = Modifier,
    viewModel: TimelineViewModel = inject(),
) {
    val viewState = viewModel.viewState.collectAsState().value
    TimelineCanvas(
        initialDate = viewState.initialDate,
        values = viewState.valuesForChart,
        onDateChange = viewModel::setDate,
        modifier = modifier.fillMaxSize(),
    )
}