package com.faltenreich.diaguard.timeline.canvas.hours

import androidx.compose.ui.geometry.Rect
import com.faltenreich.diaguard.datetime.DateTime

data class TimelineHoursState(
    val rectangle: Rect,
    val initialDateTime: DateTime,
    val hours: List<Hour>,
) {

    data class Hour(
        val x: Float,
        val hour: Int,
    )
}