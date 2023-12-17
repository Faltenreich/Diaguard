package com.faltenreich.diaguard.timeline

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun Timeline(
    modifier: Modifier = Modifier,
    viewModel: TimelineViewModel = inject(),
) {
    when (val viewState = viewModel.collectState()) {
        null -> Unit
        else -> TimelineCanvas(
            initialDate = viewState.initialDate,
            valuesForChart = viewState.valuesForChart,
            propertiesForList = viewState.propertiesForList,
            onDateChange = { viewModel.dispatchIntent(TimelineIntent.SetDate(it)) },
            modifier = modifier.fillMaxSize(),
        )
    }
}