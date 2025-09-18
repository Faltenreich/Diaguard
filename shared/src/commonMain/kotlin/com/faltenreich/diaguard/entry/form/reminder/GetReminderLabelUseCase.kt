package com.faltenreich.diaguard.entry.form.reminder

import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.reminder
import diaguard.shared.generated.resources.reminder_label_hours
import diaguard.shared.generated.resources.reminder_label_minutes
import diaguard.shared.generated.resources.reminder_label_seconds
import kotlin.time.Duration

class GetReminderLabelUseCase(
    private val localization: Localization,
) {

    operator fun invoke(duration: Duration): String {
        return duration.takeIf { it > Duration.ZERO }?.let {
            if (duration.inWholeHours > 0) {
                localization.getString(Res.string.reminder_label_hours, duration.inWholeHours)
            } else if (duration.inWholeMinutes > 0) {
                localization.getString(Res.string.reminder_label_minutes, duration.inWholeMinutes)
            } else {
                localization.getString(Res.string.reminder_label_seconds)
            }
        } ?: localization.getString(Res.string.reminder)
    }
}