package com.faltenreich.diaguard.timeline.canvas.chart

import com.faltenreich.diaguard.entry.Entry

sealed interface TapTimelineChartResult {

    data class Chart(val entry: Entry.Local): TapTimelineChartResult

    data object None : TapTimelineChartResult
}