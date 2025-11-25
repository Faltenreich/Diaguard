package com.faltenreich.diaguard.preference.reminder

import com.faltenreich.diaguard.preference.Preference
import diaguard.feature.preference.generated.resources.Res
import diaguard.feature.preference.generated.resources.preference_alarm_start

data object ReminderPreference : Preference<Long, Long> {

    override val key = Res.string.preference_alarm_start

    override val default = -1L

    override val onRead = { milliseconds: Long -> milliseconds }

    override val onWrite = { milliseconds: Long -> milliseconds }
}