package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.shared.datetime.Date

sealed interface LogIntent {

    data object CreateEntry : LogIntent

    data class SetDate(val date: Date) : LogIntent

    data class Remove(val item: LogItem.EntryContent) : LogIntent
}