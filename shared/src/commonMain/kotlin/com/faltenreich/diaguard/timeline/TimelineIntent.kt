package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date

sealed interface TimelineIntent {

    data object CreateEntry : TimelineIntent

    data object SearchEntries : TimelineIntent

    data object SelectPreviousDay : TimelineIntent

    data object SelectDate : TimelineIntent

    data object SelectNextDay : TimelineIntent

    data class SetDate(val date: Date) : TimelineIntent
}