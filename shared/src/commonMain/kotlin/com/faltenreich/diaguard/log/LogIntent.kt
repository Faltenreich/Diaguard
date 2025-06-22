package com.faltenreich.diaguard.log

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.ui.unit.IntSize
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.log.list.item.LogItemState

sealed interface LogIntent {

    data class CacheMonthHeaderSize(val size: IntSize) : LogIntent

    data class CacheDayHeaderSize(val size: IntSize) : LogIntent

    data class OnScroll(val firstItem: LogItemState, val nextItems: List<LazyListItemInfo>) : LogIntent

    data class CreateEntry(val date: Date? = null) : LogIntent

    data class OpenEntry(val entry: Entry.Local) : LogIntent

    data class OpenEntrySearch(val query: String = "") : LogIntent

    data object OpenDateDialog : LogIntent

    data object CloseDateDialog : LogIntent

    data class SetDate(val date: Date) : LogIntent

    data object SetToday : LogIntent
}