package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.preference.color.ColorScheme
import com.faltenreich.diaguard.timeline.canvas.chart.TimelineChartState
import com.faltenreich.diaguard.timeline.canvas.table.TimelineTableState

data class TimelineState(
    val initialDate: Date,
    val currentDate: Date,
    val currentDateLabel: String,
    val chart: TimelineChartState,
    val table: TimelineTableState,
    val dateDialog: DateDialog?,
    val colorScheme: ColorScheme?,
) {

    data class DateDialog(val date: Date)
}