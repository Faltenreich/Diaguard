package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.log.LogIntent

sealed interface DashboardIntent {

    data object CreateEntry : DashboardIntent

    data object SearchEntries : DashboardIntent

    data class EditEntry(val entry: Entry) : DashboardIntent
}