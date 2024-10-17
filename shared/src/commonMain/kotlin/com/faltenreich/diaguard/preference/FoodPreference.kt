package com.faltenreich.diaguard.preference

import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.preference_food_show_branded
import diaguard.shared.generated.resources.preference_food_show_common
import diaguard.shared.generated.resources.preference_food_show_custom
import org.jetbrains.compose.resources.StringResource

sealed class FoodPreference(key: StringResource) : Preference<Boolean, Boolean>(
    key = key,
    default = true,
    onRead = { it },
    onWrite = { it },
) {

    data object ShowCommonFood : FoodPreference(key = Res.string.preference_food_show_common)

    data object ShowCustomFood : FoodPreference(key = Res.string.preference_food_show_custom)

    data object ShowBrandedFood : FoodPreference(key = Res.string.preference_food_show_branded)
}