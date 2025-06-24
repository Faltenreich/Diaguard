package com.faltenreich.diaguard.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
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

    Column(modifier = modifier) {
        Box(
            modifier = Modifier.weight(1f),
        ) {
            TimelineCanvas(
                state = state,
                onIntent = viewModel::dispatchIntent,
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize(),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                    .background(AppTheme.colors.scheme.surfaceContainerLowest.copy(alpha = .6f)),
            )
        }
        TimelineDateBar(
            label = state.date.currentDateLocalized,
            onBack = { viewModel.dispatchIntent(TimelineIntent.SelectPreviousDate) },
            onForward = { viewModel.dispatchIntent(TimelineIntent.SelectNextDate) },
        )
    }

    state.date.pickerDialog?.let { dateDialog ->
        DatePickerDialog(
            date = dateDialog.date,
            onDismissRequest = { viewModel.dispatchIntent(TimelineIntent.DismissDatePicker) },
            onConfirmRequest = { date ->
                viewModel.dispatchIntent(TimelineIntent.DismissDatePicker)
                viewModel.dispatchIntent(TimelineIntent.SelectDate(date))
            },
        )
    }

    state.entryListBottomSheet?.let { valueBottomSheet ->
        ModalBottomSheet(
            onDismissRequest = { viewModel.dispatchIntent(TimelineIntent.DismissEntryListBottomSheet) },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        ) {
            TimelineEntryBottomSheet(
                entries = valueBottomSheet.entries,
                onEntryClick = { viewModel.dispatchIntent(TimelineIntent.OpenEntry(it)) },
                onTagClick = { viewModel.dispatchIntent(TimelineIntent.OpenEntrySearch(it.name)) },
            )
        }
    }
}