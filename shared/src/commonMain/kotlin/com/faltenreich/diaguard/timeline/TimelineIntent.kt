package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date

sealed interface TimelineIntent {

    data object OpenMainMenu : TimelineIntent

    data object CreateEntry : TimelineIntent

    data object SearchEntries : TimelineIntent

    data object ShowDatePicker : TimelineIntent

    data object MoveDayBack : TimelineIntent

    data object MoveDayForward : TimelineIntent

    data class SetCurrentDate(val currentDate: Date) : TimelineIntent
}