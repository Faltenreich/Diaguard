package com.faltenreich.diaguard.log.item

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.log.LogKey
import com.faltenreich.diaguard.shared.datetime.Date

sealed class LogItem(val date: Date, val style: LogDayStyle, val key: LogKey) {

    class MonthHeader(
        date: Date,
    ) : LogItem(
        date = date,
        style = LogDayStyle(isVisible = false, isHighlighted = false),
        key = LogKey.Header(date),
    ) {

        override fun toString(): String {
            return "Month: $date"
        }
    }

    class EntryContent(
        val entry: Entry,
        style: LogDayStyle,
    ) : LogItem(
        date = entry.dateTime.date,
        style = style,
        key = LogKey.Item(entry.dateTime.date, isFirstOfDay = style.isVisible),
    ) {

        override fun toString(): String {
            return "Entry: ${entry.dateTime}"
        }
    }

    class EmptyContent(
        date: Date,
        style: LogDayStyle,
    ) : LogItem(
        date = date,
        style = style,
        key = LogKey.Item(date, isFirstOfDay = true),
    ) {

        override fun toString(): String {
            return "Empty: $date"
        }
    }
}