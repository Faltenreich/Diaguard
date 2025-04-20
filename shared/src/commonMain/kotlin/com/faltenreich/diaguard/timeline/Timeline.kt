package com.faltenreich.diaguard.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.datetime.picker.DatePicker
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvas

@Composable
fun Timeline(
    viewModel: TimelineViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState() ?: return

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .background(AppTheme.colors.scheme.background),
        )
        TimelineCanvas(
            state = state,
            onIntent = viewModel::dispatchIntent,
            viewModel = viewModel,
            modifier = Modifier.weight(1f),
        )
        TimelineDateBar(
            label = state.currentDateLabel,
            onIntent = viewModel::dispatchIntent,
        )
    }

    state.dateDialog?.let { dateDialog ->
        DatePicker(
            date = dateDialog.date,
            onPick = { date ->
                viewModel.dispatchIntent(TimelineIntent.CloseDateDialog)
                viewModel.postEvent(TimelineEvent.DateSelected(date))
            },
        )
    }
}