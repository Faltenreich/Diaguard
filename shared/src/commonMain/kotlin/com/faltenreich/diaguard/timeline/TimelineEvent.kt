package com.faltenreich.diaguard.timeline

sealed interface TimelineEvent {

    data object SelectedDate : TimelineEvent
}