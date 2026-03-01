package com.faltenreich.diaguard.export.preference

import com.faltenreich.diaguard.data.preference.Preference

data object IncludeNotesPreference : Preference<Boolean, Boolean> {

    override val key = "preference_export_include_notes"

    override val default = true

    override val onRead = { value: Boolean -> value }

    override val onWrite = { value: Boolean -> value }
}