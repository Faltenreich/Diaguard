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
    val colorStops: List<ColorStop>,
) {

    data class Hour(
        val x: Float,
        val hour: Int,
    )

    data class ColorStop(
        val offset: Float,
        val type: Type,
    ) {

        enum class Type {
            INVISIBLE,
            VISIBLE,
        }
    }
}