package com.faltenreich.diaguard.preference.food

import com.faltenreich.diaguard.preference.Preference
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.preference_food_show_common

data object ShowCommonFoodPreference : Preference<Boolean, Boolean> {

    override val default = true

    override val onRead = { value: Boolean -> value }

    override val onWrite = { value: Boolean -> value }

    override val key = Res.string.preference_food_show_common
}