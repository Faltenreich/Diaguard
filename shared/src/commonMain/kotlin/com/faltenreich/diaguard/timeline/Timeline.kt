package com.faltenreich.diaguard.timeline

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvas

@Composable
fun Timeline(
    modifier: Modifier = Modifier,
    viewModel: TimelineViewModel = inject(),
) {
    val state = viewModel.collectState() ?: return

    Column(modifier = modifier) {
        TimelineCanvas(
            state = state,
            viewModel = viewModel,
            modifier = Modifier.weight(1f),
        )
        TimelineDateBar(
            label = state.currentDateLabel,
            onIntent = { viewModel.dispatchIntent(it) },
            modifier = Modifier.height(AppTheme.dimensions.size.TouchSizeLarge),
        )
    }
}