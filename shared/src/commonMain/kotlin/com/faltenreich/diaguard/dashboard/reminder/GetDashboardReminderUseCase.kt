package com.faltenreich.diaguard.dashboard.reminder

import com.faltenreich.diaguard.entry.form.reminder.GetReminderLabelUseCase
import com.faltenreich.diaguard.entry.form.reminder.GetReminderUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetDashboardReminderUseCase(
    private val getReminder: GetReminderUseCase,
    private val getLabel: GetReminderLabelUseCase,
) {

    operator fun invoke(): Flow<DashboardReminderState?> {
        return getReminder().map { duration ->
            when (duration) {
                null -> null
                else -> DashboardReminderState(text = getLabel(duration))
            }
        }
    }
}