package com.faltenreich.diaguard.export.preference

import com.faltenreich.diaguard.data.preference.Preference

data object IncludeCalendarWeekPreference : Preference<Boolean, Boolean> {

    override val key = "preference_export_include_calendar_week"

    override val default = true

    override val onRead = { value: Boolean -> value }

    override val onWrite = { value: Boolean -> value }
}