package com.faltenreich.diaguard.food.nutrient

import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.carbohydrates
import com.faltenreich.diaguard.resource.energy
import com.faltenreich.diaguard.resource.fat
import com.faltenreich.diaguard.resource.fat_saturated
import com.faltenreich.diaguard.resource.fiber
import com.faltenreich.diaguard.resource.proteins
import com.faltenreich.diaguard.resource.salt
import com.faltenreich.diaguard.resource.sodium
import com.faltenreich.diaguard.resource.sugar
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