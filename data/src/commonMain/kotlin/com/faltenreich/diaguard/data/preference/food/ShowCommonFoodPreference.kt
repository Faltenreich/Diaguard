package com.faltenreich.diaguard.data.preference.food

import com.faltenreich.diaguard.data.preference.Preference

data object ShowCommonFoodPreference : Preference<Boolean, Boolean> {

    override val key = "showCommonFood"

    override val default = true

    override val onRead = { value: Boolean -> value }

    override val onWrite = { value: Boolean -> value }
}