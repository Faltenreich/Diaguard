package com.faltenreich.diaguard.entry.form.reminder

import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.reminder
import com.faltenreich.diaguard.resource.reminder_label_hours
import com.faltenreich.diaguard.resource.reminder_label_minutes
import com.faltenreich.diaguard.resource.reminder_label_seconds
import kotlin.time.Duration

class GetReminderLabelUseCase(
    private val localization: Localization,
) {

    operator fun invoke(duration: Duration): String {
        return duration.takeIf { it > Duration.ZERO }?.let {
            if (duration.inWholeHours > 0) {
                val quantity = duration.inWholeHours.toInt()
                localization.getPluralString(Res.plurals.reminder_label_hours, quantity, quantity)
            } else if (duration.inWholeMinutes > 0) {
                val quantity = duration.inWholeMinutes.toInt()
                localization.getPluralString(Res.plurals.reminder_label_minutes, quantity, quantity)
            } else {
                localization.getString(Res.string.reminder_label_seconds)
            }
        } ?: localization.getString(Res.string.reminder)
    }
}