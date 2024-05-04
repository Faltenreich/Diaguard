package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date

sealed interface TimelineEvent {

    data class DateSelected(val date: Date) : TimelineEvent
}