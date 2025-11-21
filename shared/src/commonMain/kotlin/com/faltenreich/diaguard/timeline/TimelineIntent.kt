package com.faltenreich.diaguard.timeline

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.entry.list.EntryListItemState

sealed interface TimelineIntent {

    data class Setup(
        val canvasSize: Size,
        val tableRowHeight: Float,
        val statusBarHeight: Int,
    ) : TimelineIntent

    data class Invalidate(
        val scrollOffset: Float,
        val state: TimelineState,
        val config: TimelineConfig,
    ) : TimelineIntent

    data class TapCanvas(
        val position: Offset,
        val touchAreaSize: Size,
    ) : TimelineIntent

    data class SelectDate(val date: Date) : TimelineIntent

    data object SelectPreviousDate : TimelineIntent

    data object SelectNextDate : TimelineIntent

    data object OpenDatePickerDialog : TimelineIntent

    data object CloseDatePickerDialog : TimelineIntent

    data object CreateEntry : TimelineIntent

    data class OpenEntry(val entry: Entry.Local) : TimelineIntent

    data class DeleteEntry(val entry: Entry.Local) : TimelineIntent

    data class RestoreEntry(val entry: EntryListItemState) : TimelineIntent

    data class OpenEntryListBottomSheet(val entries: List<EntryListItemState>) : TimelineIntent

    data object DismissEntryListBottomSheet : TimelineIntent

    data class OpenEntrySearch(val query: String = "") : TimelineIntent
}