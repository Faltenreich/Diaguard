package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date

sealed interface TimelineIntent {

    data class Invalidate(
        val scrollOffset: Float,
        val state: TimelineState,
        val config: TimelineConfig,
    ) : TimelineIntent

    data class SelectDate(val date: Date) : TimelineIntent

    data object SelectPreviousDate : TimelineIntent

    data object SelectNextDate : TimelineIntent

    data object CreateEntry : TimelineIntent

    data object SearchEntries : TimelineIntent
}