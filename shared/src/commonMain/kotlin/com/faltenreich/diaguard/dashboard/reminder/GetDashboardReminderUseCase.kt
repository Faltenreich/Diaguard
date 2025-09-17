package com.faltenreich.diaguard.dashboard.reminder

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetDashboardReminderUseCase {

    operator fun invoke(): Flow<DashboardReminderState?> {
        // TODO
        return flowOf(DashboardReminderState(text = "Reminder in  5 minutes"))
    }
}