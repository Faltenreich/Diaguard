package com.faltenreich.diaguard.food.nutrient

import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.StringResource

enum class FoodNutrient(val label: StringResource) {
    CARBOHYDRATES(MR.strings.carbohydrates),
    ENERGY(MR.strings.energy),
    FAT(MR.strings.fat),
    FAT_SATURATED(MR.strings.fat_saturated),
    FIBER(MR.strings.fiber),
    PROTEINS(MR.strings.proteins),
    SALT(MR.strings.salt),
    SODIUM(MR.strings.sodium),
    SUGAR(MR.strings.sugar),
}