package com.faltenreich.diaguard.log.item

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.datetime.Date

sealed class LogItem(val date: Date, val key: Any) {

    class MonthHeader(
        date: Date,
    ) : LogItem(date = date, key = "Month$date") {
        override fun toString(): String {
            return "Month: $date"
        }
    }

    class EntryContent(
        val entry: Entry,
        val style: LogDayStyle,
    ) : LogItem(date = entry.dateTime.date, key = "Entry${entry.id}") {
        override fun toString(): String {
            return "Entry: ${entry.dateTime}"
        }
    }

    class EmptyContent(
        date: Date,
    ) : LogItem(date = date, key = "Empty$date") {
        override fun toString(): String {
            return "Empty: $date"
        }
    }
}