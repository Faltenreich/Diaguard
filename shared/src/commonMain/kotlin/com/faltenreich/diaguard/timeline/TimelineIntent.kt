package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.shared.datetime.Date

sealed interface TimelineIntent {

    data class SetDate(val date: Date) : TimelineIntent
}