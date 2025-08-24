package com.faltenreich.diaguard.entry.form.reminder

import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.reminder
import diaguard.shared.generated.resources.reminder_label
import kotlin.time.Duration

class GetReminderLabelUseCase(
    private val localization: Localization,
) {

    operator fun invoke(duration: Duration): String {
        return duration.takeIf { it > Duration.ZERO }?.let {
            localization.getString(Res.string.reminder_label, duration.inWholeMinutes)
        } ?: localization.getString(Res.string.reminder)
    }
}