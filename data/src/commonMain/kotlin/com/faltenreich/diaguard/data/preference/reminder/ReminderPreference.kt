package com.faltenreich.diaguard.data.preference.reminder

import com.faltenreich.diaguard.data.preference.Preference
import diaguard.data.generated.resources.Res
import diaguard.data.generated.resources.preference_alarm_start

data object ReminderPreference : Preference<Long, Long> {

    override val key = Res.string.preference_alarm_start

    override val default = -1L

    override val onRead = { milliseconds: Long -> milliseconds }

    override val onWrite = { milliseconds: Long -> milliseconds }
}