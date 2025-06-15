package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.timeline.canvas.TimelineCanvasState
import com.faltenreich.diaguard.timeline.date.TimelineDateState

data class TimelineState(
    val date: TimelineDateState,
    val canvas: TimelineCanvasState?,
)