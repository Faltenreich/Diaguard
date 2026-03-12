package com.faltenreich.diaguard.timeline.canvas.time

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvasDimensions

data class TimelineTimeState(
    val dimensions: TimelineCanvasDimensions.Calculated,
    val currentDate: Date,
    val initialDateTime: DateTime,
    val hourProgression: IntProgression,
    val hours: List<Hour>,
) {

    data class Hour(
        val x: Float,
        val hour: Int,
        val hourLocalized: String,
    )
}