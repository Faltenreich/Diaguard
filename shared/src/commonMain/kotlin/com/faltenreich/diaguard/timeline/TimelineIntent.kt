package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.shared.datetime.Date

sealed interface TimelineIntent {

    data object CreateEntry : TimelineIntent

    data object SearchEntries : TimelineIntent

    data class SetDate(val date: Date) : TimelineIntent
}