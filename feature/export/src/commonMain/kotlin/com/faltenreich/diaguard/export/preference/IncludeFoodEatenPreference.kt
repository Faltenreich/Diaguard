package com.faltenreich.diaguard.export.preference

import com.faltenreich.diaguard.data.preference.Preference

data object IncludeFoodEatenPreference : Preference<Boolean, Boolean> {

    override val key = "preference_export_include_food_eaten"

    override val default = true

    override val onRead = { value: Boolean -> value }

    override val onWrite = { value: Boolean -> value }
}