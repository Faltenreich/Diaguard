package com.faltenreich.diaguard.entry.form.reminder

import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.reminder
import diaguard.shared.generated.resources.reminder_label

class GetReminderLabelUseCase(
    private val localization: Localization,
) {

    operator fun invoke(reminderDelayInMinutes: Int?): String {
        return reminderDelayInMinutes?.let {
            // TODO: Differentiate between minutes, hours, days
            val delay = "$reminderDelayInMinutes minutes"
            localization.getString(Res.string.reminder_label, delay)
        } ?: localization.getString(Res.string.reminder)
    }
}