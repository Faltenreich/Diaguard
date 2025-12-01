package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.entry.list.EntryListItemState
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvasState
import com.faltenreich.diaguard.timeline.date.TimelineDateState

data class TimelineState(
    val date: TimelineDateState,
    val canvas: TimelineCanvasState?,
    val entryListBottomSheet: EntryListBottomSheet?,
) {

    data class EntryListBottomSheet(val entries: List<EntryListItemState>)
}