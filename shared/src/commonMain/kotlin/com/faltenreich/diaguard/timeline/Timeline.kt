package com.faltenreich.diaguard.timeline

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.timeline.chart.TimelineChart
import com.faltenreich.diaguard.timeline.list.TimelineList

@Composable
fun Timeline(
    modifier: Modifier = Modifier,
    viewModel: TimelineViewModel = inject(),
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        TimelineChart(
            modifier = Modifier.weight(2f),
        )
        TimelineList(
            modifier = Modifier.weight(1f),
        )
    }
}