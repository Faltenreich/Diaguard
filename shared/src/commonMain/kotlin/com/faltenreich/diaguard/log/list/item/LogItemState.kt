package com.faltenreich.diaguard.log.list.item

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.entry.list.EntryListItemState
import com.faltenreich.diaguard.log.list.LogKey

sealed interface LogItemState {

    val date: Date
    val dayOfMonthLocalized: String
    val dayOfWeekLocalized: String
    val style: LogDayStyle
    val key: LogKey

    data class MonthHeader(
        override val date: Date,
        override val dayOfMonthLocalized: String,
        override val dayOfWeekLocalized: String,
        val dateLocalized: String,
    ) : LogItemState {

        override val style = LogDayStyle(isVisible = false, isHighlighted = false)
        override val key = LogKey.Header(date)

        override fun toString(): String {
            return "Month: $date"
        }
    }

    data class EntryContent(
        override val dayOfMonthLocalized: String,
        override val dayOfWeekLocalized: String,
        override val style: LogDayStyle,
        val entryState: EntryListItemState,
    ) : LogItemState {

        override val date = entryState.entry.dateTime.date
        override val key = LogKey.Item(entryState.entry.dateTime.date, isFirstOfDay = style.isVisible)

        override fun toString(): String {
            return "Entry: ${entryState.entry.dateTime}"
        }
    }

    data class EmptyContent(
        override val date: Date,
        override val dayOfMonthLocalized: String,
        override val dayOfWeekLocalized: String,
        override val style: LogDayStyle,
    ) : LogItemState {

        override val key = LogKey.Item(date, isFirstOfDay = true)

        override fun toString(): String {
            return "Empty: $date"
        }
    }
}