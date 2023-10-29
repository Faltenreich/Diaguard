package com.faltenreich.diaguard.food.detail

import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.StringResource

enum class FoodDetailTab(
    val label: StringResource,
) {
    INFO(MR.strings.food_info),
    NUTRIENT_LIST(MR.strings.food_nutrient_list),
    EATEN_LIST(MR.strings.food_eaten_list),
}