package com.faltenreich.diaguard.export.preference

import com.faltenreich.diaguard.data.preference.Preference

data object IncludeDateOfExportPreference : Preference<Boolean, Boolean> {

    override val key = "preference_export_include_date_of_export"

    override val default = true

    override val onRead = { value: Boolean -> value }

    override val onWrite = { value: Boolean -> value }
}