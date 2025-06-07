package com.faltenreich.diaguard.timeline

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.datetime.picker.DatePickerDialog
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvas
import com.faltenreich.diaguard.timeline.date.TimelineDateBar

@Composable
fun Timeline(
    viewModel: TimelineViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState() ?: return

    Column(modifier = modifier) {
        TimelineCanvas(
            state = state,
            onIntent = viewModel::dispatchIntent,
            viewModel = viewModel,
            modifier = Modifier.weight(1f),
        )
        TimelineDateBar(
            label = state.date.label,
            onIntent = viewModel::dispatchIntent,
        )
    }

    state.date.pickerDialog?.let { dateDialog ->
        DatePickerDialog(
            date = dateDialog.date,
            onDismissRequest = { viewModel.dispatchIntent(TimelineIntent.CloseDateDialog) },
            onConfirmRequest = { date ->
                viewModel.dispatchIntent(TimelineIntent.CloseDateDialog)
                viewModel.postEvent(TimelineEvent.DateSelected(date))
            },
        )
    }
}