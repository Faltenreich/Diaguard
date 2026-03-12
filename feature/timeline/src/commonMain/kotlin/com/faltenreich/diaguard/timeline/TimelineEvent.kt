package com.faltenreich.diaguard.timeline

sealed interface TimelineEvent {

    data class Scroll(val offset: Float) : TimelineEvent
}