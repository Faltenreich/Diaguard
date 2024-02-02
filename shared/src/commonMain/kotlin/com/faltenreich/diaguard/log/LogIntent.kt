package com.faltenreich.diaguard.log

import androidx.compose.foundation.lazy.LazyListItemInfo
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.shared.datetime.Date

sealed interface LogIntent {

    data class SetMonthHeaderHeight(val monthHeaderHeight: Int) : LogIntent

    data class SetDayHeaderHeight(val dayHeaderHeight: Int) : LogIntent

    data class InvalidateStickyHeaderInfo(
        val firstItem: LogItem,
        val nextItems: List<LazyListItemInfo>,
    ) : LogIntent

    data class CreateEntry(val date: Date? = null) : LogIntent

    data class OpenEntry(val entry: Entry) : LogIntent

    data object SearchEntries : LogIntent

    data class SetDate(val date: Date) : LogIntent

    data class Remove(val item: LogItem.EntryContent) : LogIntent
}