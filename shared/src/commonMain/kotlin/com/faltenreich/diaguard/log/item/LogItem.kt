package com.faltenreich.diaguard.log.item

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.log.LogKey
import com.faltenreich.diaguard.shared.datetime.Date

sealed class LogItem(val date: Date, val key: LogKey) {

    class MonthHeader(
        date: Date,
    ) : LogItem(date = date, key = LogKey.Header(date)) {

        override fun toString(): String {
            return "Month: $date"
        }
    }

    class EntryContent(
        val entry: Entry,
        val style: LogDayStyle,
    ) : LogItem(
        date = entry.dateTime.date,
        key = LogKey.Item(entry.dateTime.date, isFirstOfDay = style != LogDayStyle.HIDDEN),
    ) {

        override fun toString(): String {
            return "Entry: ${entry.dateTime}"
        }
    }

    class EmptyContent(
        date: Date,
        val style: LogDayStyle,
    ) : LogItem(
        date = date,
        key = LogKey.Item(date, isFirstOfDay = true),
    ) {

        override fun toString(): String {
            return "Empty: $date"
        }
    }
}