package com.faltenreich.diaguard.dashboard.reminder

import com.faltenreich.diaguard.entry.form.reminder.GetReminderLabelUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Duration.Companion.minutes

class GetDashboardReminderUseCase(
    private val getLabel: GetReminderLabelUseCase,
) {

    operator fun invoke(): Flow<DashboardReminderState?> {
        // TODO: Get previous alarm via DataStore
        val duration = 5.minutes
        val label = getLabel(duration)
        return flowOf(DashboardReminderState(text = label))
    }
}