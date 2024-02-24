package com.faltenreich.diaguard.log

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.ui.unit.IntSize
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.shared.datetime.Date

sealed interface LogIntent {

    data class CacheMonthHeaderSize(val size: IntSize) : LogIntent

    data class CacheDayHeaderSize(val size: IntSize) : LogIntent

    data class OnScroll(val firstItem: LogItem, val nextItems: List<LazyListItemInfo>) : LogIntent

    data class CreateEntry(val date: Date? = null) : LogIntent

    data class OpenEntry(val entry: Entry) : LogIntent

    data object SearchEntries : LogIntent

    data class SetDate(val date: Date) : LogIntent
}