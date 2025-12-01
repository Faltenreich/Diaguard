package com.faltenreich.diaguard.timeline.canvas

import com.faltenreich.diaguard.timeline.canvas.chart.TimelineChartState
import com.faltenreich.diaguard.timeline.canvas.table.TimelineTableState
import com.faltenreich.diaguard.timeline.canvas.time.TimelineTimeState

data class TimelineCanvasState(
    val time: TimelineTimeState,
    val chart: TimelineChartState,
    val table: TimelineTableState,
)