package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.timeline.canvas.chart.TimelineChartState
import com.faltenreich.diaguard.timeline.canvas.hours.TimelineHoursState
import com.faltenreich.diaguard.timeline.canvas.table.TimelineTableState
import com.faltenreich.diaguard.timeline.date.TimelineDateState

data class TimelineState(
    val date: TimelineDateState,
    val hours: TimelineHoursState,
    val chart: TimelineChartState,
    val table: TimelineTableState,
)