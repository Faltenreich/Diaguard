package com.faltenreich.diaguard.timeline.canvas

import com.faltenreich.diaguard.entry.list.EntryListItemState

sealed interface TapTimelineCanvasResult {

    data class Chart(val entries: List<EntryListItemState>): TapTimelineCanvasResult

    data class Table(val entries: List<EntryListItemState>): TapTimelineCanvasResult

    data object None : TapTimelineCanvasResult
}