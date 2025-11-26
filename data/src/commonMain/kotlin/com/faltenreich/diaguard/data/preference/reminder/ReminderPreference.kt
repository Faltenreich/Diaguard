package com.faltenreich.diaguard.data.preference.reminder

import com.faltenreich.diaguard.data.preference.Preference

data object ReminderPreference : Preference<Long, Long> {

    override val key = "alarmStartInMillis"

    override val default = -1L

    override val onRead = { milliseconds: Long -> milliseconds }

    override val onWrite = { milliseconds: Long -> milliseconds }
}