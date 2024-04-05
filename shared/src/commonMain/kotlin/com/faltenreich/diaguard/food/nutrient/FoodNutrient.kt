package com.faltenreich.diaguard.food.nutrient

import diaguard.shared.generated.resources.*
import org.jetbrains.compose.resources.StringResource

enum class FoodNutrient(val label: StringResource) {
    CARBOHYDRATES(Res.string.carbohydrates),
    ENERGY(Res.string.energy),
    FAT(Res.string.fat),
    FAT_SATURATED(Res.string.fat_saturated),
    FIBER(Res.string.fiber),
    PROTEINS(Res.string.proteins),
    SALT(Res.string.salt),
    SODIUM(Res.string.sodium),
    SUGAR(Res.string.sugar),
}