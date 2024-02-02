package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.log.item.LogDayStickyHeaderInfo
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.shared.datetime.Date

sealed interface LogIntent {

    data class SetMonthHeaderHeight(val monthHeaderHeight: Int) : LogIntent

    data class SetDayHeaderHeight(val dayHeaderHeight: Int) : LogIntent

    data class SetStickyHeaderInfo(val stickyHeaderInfo: LogDayStickyHeaderInfo) : LogIntent

    data class CreateEntry(val date: Date? = null) : LogIntent

    data class OpenEntry(val entry: Entry) : LogIntent

    data object SearchEntries : LogIntent

    data class SetDate(val date: Date) : LogIntent

    data class Remove(val item: LogItem.EntryContent) : LogIntent
}