package com.faltenreich.diaguard.data.preference.food

import com.faltenreich.diaguard.data.preference.Preference

data object ShowBrandedFoodPreference : Preference<Boolean, Boolean> {

    override val key = "showBrandedFood"

    override val default = true

    override val onRead = { value: Boolean -> value }

    override val onWrite = { value: Boolean -> value }
}