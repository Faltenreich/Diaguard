package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.timeline.canvas.chart.TimelineChartState
import com.faltenreich.diaguard.timeline.canvas.table.TimelineTableState
import com.faltenreich.diaguard.timeline.date.TimelineDateState

data class TimelineState(
    val chart: TimelineChartState,
    val table: TimelineTableState,
    val date: TimelineDateState,
) {

    data class DateDialog(val date: Date)
}