package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.timeline.canvas.TimelineCoordinates

sealed interface TimelineIntent {

    data class Invalidate(
        val currentDate: Date,
        val coordinates: TimelineCoordinates,
    ) : TimelineIntent

    data class SelectDate(val date: Date) : TimelineIntent

    data object SelectPreviousDate : TimelineIntent

    data object SelectNextDate : TimelineIntent

    data object CreateEntry : TimelineIntent

    data object SearchEntries : TimelineIntent
}