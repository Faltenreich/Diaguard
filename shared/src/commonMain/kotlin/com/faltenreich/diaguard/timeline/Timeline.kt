package com.faltenreich.diaguard.timeline

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.datetime.picker.DatePickerDialog
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvas
import com.faltenreich.diaguard.timeline.date.TimelineDateBar
import com.faltenreich.diaguard.timeline.entry.TimelineEntryBottomSheet

@Composable
fun Timeline(
    viewModel: TimelineViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState() ?: return
    var showDatePicker by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        TimelineCanvas(
            state = state,
            onIntent = viewModel::dispatchIntent,
            viewModel = viewModel,
            modifier = Modifier.weight(1f),
        )
        TimelineDateBar(
            label = state.date.currentDateLocalized,
            onBack = { viewModel.dispatchIntent(TimelineIntent.SelectPreviousDate) },
            onPick = { showDatePicker = true },
            onForward = { viewModel.dispatchIntent(TimelineIntent.SelectNextDate) },
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            date = state.date.currentDate,
            onDismissRequest = { showDatePicker = false },
            onConfirmRequest = { date ->
                showDatePicker = false
                viewModel.dispatchIntent(TimelineIntent.SelectDate(date))
            },
        )
    }

    state.valueBottomSheet?.let { valueBottomSheet ->
        ModalBottomSheet(
            onDismissRequest = { viewModel.dispatchIntent(TimelineIntent.DismissValueBottomSheet) },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        ) {
            TimelineEntryBottomSheet(values = valueBottomSheet.values)
        }
    }
}