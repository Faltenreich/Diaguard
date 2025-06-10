package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.timeline.canvas.TimelineCoordinates
import com.faltenreich.diaguard.timeline.canvas.chart.TimelineChartState
import com.faltenreich.diaguard.timeline.canvas.table.TimelineTableState
import com.faltenreich.diaguard.timeline.date.TimelineDateState

data class TimelineState(
    val date: TimelineDateState,
    val chart: TimelineChartState,
    val table: TimelineTableState,
    val coordinates: TimelineCoordinates?,
)