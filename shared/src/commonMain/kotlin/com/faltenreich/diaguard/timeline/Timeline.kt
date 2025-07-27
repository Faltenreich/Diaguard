package com.faltenreich.diaguard.timeline

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
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
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.faltenreich.diaguard.shared.view.rememberAnimatable
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvas
import com.faltenreich.diaguard.timeline.date.TimelineDateBar
import com.faltenreich.diaguard.timeline.date.TimelineDateState
import com.faltenreich.diaguard.timeline.entry.TimelineEntryBottomSheet
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Timeline(
    state: TimelineState?,
    scrollOffset: Animatable<Float, AnimationVector1D>,
    onIntent: (TimelineIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    state ?: return

    Column(modifier = modifier) {
        Box(
            modifier = Modifier.weight(1f),
        ) {
            TimelineCanvas(
                state = state,
                scrollOffset = scrollOffset,
                onIntent = onIntent,
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
            onBack = { onIntent(TimelineIntent.SelectPreviousDate) },
            onForward = { onIntent(TimelineIntent.SelectNextDate) },
        )
    }

    state.date.datePickerDialog?.let { datePickerDialog ->
        DatePickerDialog(
            date = datePickerDialog.date,
            onDismissRequest = { onIntent(TimelineIntent.CloseDatePickerDialog) },
            onConfirmRequest = { date ->
                onIntent(TimelineIntent.CloseDatePickerDialog)
                onIntent(TimelineIntent.SelectDate(date))
            },
        )
    }

    state.entryListBottomSheet?.let { valueBottomSheet ->
        ModalBottomSheet(
            onDismissRequest = { onIntent(TimelineIntent.DismissEntryListBottomSheet) },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        ) {
            TimelineEntryBottomSheet(
                entries = valueBottomSheet.entries,
                onEntryClick = { onIntent(TimelineIntent.OpenEntry(it)) },
                onTagClick = { onIntent(TimelineIntent.OpenEntrySearch(it.name)) },
            )
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    val date = today()
    Timeline(
        state = TimelineState(
            date = TimelineDateState(
                initialDate = date,
                currentDate = date,
                currentDateLocalized = date.toString(),
                datePickerDialog = null,
            ),
            canvas = null,
            entryListBottomSheet = null,
        ),
        scrollOffset = rememberAnimatable(),
        onIntent = {},
    )
}