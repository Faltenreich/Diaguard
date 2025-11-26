package com.faltenreich.diaguard.data.preference.food

import com.faltenreich.diaguard.data.preference.Preference

data object ShowCustomFoodPreference : Preference<Boolean, Boolean> {

    override val key = "showCustomFood"

    override val default = true

    override val onRead = { value: Boolean -> value }

    override val onWrite = { value: Boolean -> value }
}