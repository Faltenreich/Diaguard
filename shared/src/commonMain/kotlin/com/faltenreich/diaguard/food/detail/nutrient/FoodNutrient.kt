package com.faltenreich.diaguard.food.detail.nutrient

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.food.Food
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
    ;

    fun fromFood(food: Food): Double? {
        return when (this) {
            CARBOHYDRATES -> food.carbohydrates
            SUGAR -> food.sugar
            ENERGY -> food.energy
            FAT -> food.fat
            FAT_SATURATED -> food.fatSaturated
            FIBER -> food.fiber
            PROTEINS -> food.proteins
            SALT -> food.salt
            SODIUM -> food.sodium
        }
    }
}