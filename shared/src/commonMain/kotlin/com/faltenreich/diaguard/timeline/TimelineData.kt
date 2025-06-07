package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.timeline.canvas.chart.TimelineChartState
import com.faltenreich.diaguard.timeline.canvas.table.TimelineTableState

data class TimelineData(
    val chart: TimelineChartState,
    val table: TimelineTableState,
)