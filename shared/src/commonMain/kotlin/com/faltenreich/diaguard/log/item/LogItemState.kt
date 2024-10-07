package com.faltenreich.diaguard.log.item

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.list.DateListItemStyle
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.log.LogKey

sealed interface LogItemState {

    val date: Date
    val style: DateListItemStyle
    val key: LogKey

    data class MonthHeader(
        override val date: Date,
    ) : LogItemState {

        override val style = DateListItemStyle(isVisible = false, isHighlighted = false)
        override val key = LogKey.Header(date)

        override fun toString(): String {
            return "Month: $date"
        }
    }

    data class EntryContent(
        val entry: Entry.Local,
        override val style: DateListItemStyle,
    ) : LogItemState {

        override val date = entry.dateTime.date
        override val key = LogKey.Item(entry.dateTime.date, isFirstOfDay = style.isVisible)

        override fun toString(): String {
            return "Entry: ${entry.dateTime}"
        }
    }

    data class EmptyContent(
        override val date: Date,
        override val style: DateListItemStyle,
    ) : LogItemState {

        override val key = LogKey.Item(date, isFirstOfDay = true)

        override fun toString(): String {
            return "Empty: $date"
        }
    }
}