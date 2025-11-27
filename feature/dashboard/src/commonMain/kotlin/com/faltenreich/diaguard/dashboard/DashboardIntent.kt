package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.data.entry.Entry

sealed interface DashboardIntent {

    data object CreateEntry : DashboardIntent

    data class EditEntry(val entry: Entry.Local) : DashboardIntent

    data object OpenStatistic : DashboardIntent

    data object SearchEntries : DashboardIntent

    data object DeleteReminder : DashboardIntent
}