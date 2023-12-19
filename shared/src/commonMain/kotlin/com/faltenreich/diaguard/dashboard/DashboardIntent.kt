package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.entry.Entry

sealed interface DashboardIntent {

    data object CreateBloodSugar : DashboardIntent

    data class EditBloodSugar(val entry: Entry) : DashboardIntent
}