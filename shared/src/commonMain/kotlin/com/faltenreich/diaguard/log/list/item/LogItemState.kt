package com.faltenreich.diaguard.log.list.item

import com.faltenreich.diaguard.entry.list.EntryListItemState
import com.faltenreich.diaguard.log.list.LogKey

sealed interface LogItemState {

    val key: LogKey
    val dayState: LogDayState

    data class MonthHeader(
        override val dayState: LogDayState,
        val dateLocalized: String,
    ) : LogItemState {

        override val key = LogKey.Header(dayState.date)

        override fun toString(): String {
            return "Month: ${dayState.date}"
        }
    }

    data class EntryContent(
        override val dayState: LogDayState,
        val entryState: EntryListItemState,
    ) : LogItemState {

        override val key = LogKey.Item(entryState.entry.dateTime.date, isFirstOfDay = dayState.style.isVisible)

        override fun toString(): String {
            return "Entry: ${entryState.entry.dateTime}"
        }
    }

    data class EmptyContent(
        override val dayState: LogDayState,
    ) : LogItemState {

        override val key = LogKey.Item(dayState.date, isFirstOfDay = true)

        override fun toString(): String {
            return "Empty: ${dayState.date}"
        }
    }
}