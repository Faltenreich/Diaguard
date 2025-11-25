package com.faltenreich.diaguard.data.preference.food

import com.faltenreich.diaguard.data.preference.Preference
import diaguard.data.generated.resources.Res
import diaguard.data.generated.resources.preference_food_show_common

data object ShowCommonFoodPreference :
    Preference<Boolean, Boolean> {

    override val default = true

    override val onRead = { value: Boolean -> value }

    override val onWrite = { value: Boolean -> value }

    override val key = Res.string.preference_food_show_common
}