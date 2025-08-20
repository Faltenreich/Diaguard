package com.faltenreich.diaguard.entry.form.alarm

import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.reminder
import diaguard.shared.generated.resources.reminder_label

class GetAlarmLabelUseCase(
    private val localization: Localization,
) {

    operator fun invoke(alarmDelayInMinutes: Int?): String {
        return alarmDelayInMinutes?.let {
            localization.getString(Res.string.reminder_label, alarmDelayInMinutes)
        } ?: localization.getString(Res.string.reminder)
    }
}