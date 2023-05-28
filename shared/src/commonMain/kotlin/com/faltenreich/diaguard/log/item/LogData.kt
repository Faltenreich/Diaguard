package com.faltenreich.diaguard.log.item

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.datetime.Date

sealed class LogData(val date: Date, val key: Any) {

    class MonthHeader(date: Date) : LogData(date = date, key = "Month$date")

    class DayHeader(date: Date) : LogData(date = date, key = "Day$date")

    class EntryContent(val entry: Entry) : LogData(date = entry.dateTime.date, key = "Entry${entry.id}")

    class EmptyContent(date: Date) : LogData(date = date, key = "Empty$date")
}