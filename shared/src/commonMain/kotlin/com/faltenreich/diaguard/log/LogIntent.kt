package com.faltenreich.diaguard.log

import androidx.compose.foundation.lazy.LazyListItemInfo
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.log.list.item.LogItemState

sealed interface LogIntent {

    data class OnScroll(
        val firstItem: LogItemState,
        val nextItems: List<LazyListItemInfo>,
        val dayHeaderHeight: Int,
    ) : LogIntent

    data class CreateEntry(val date: Date? = null) : LogIntent

    data class OpenEntry(val entry: Entry.Local) : LogIntent

    data class OpenEntrySearch(val query: String = "") : LogIntent

    data object OpenDatePickerDialog : LogIntent

    data object CloseDatePickerDialog : LogIntent

    data class SetDate(val date: Date) : LogIntent
}