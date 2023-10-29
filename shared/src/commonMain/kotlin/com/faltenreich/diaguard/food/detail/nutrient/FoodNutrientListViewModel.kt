package com.faltenreich.diaguard.food.detail.nutrient

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.detail.nutrient.FoodNutrient.CARBOHYDRATES
import com.faltenreich.diaguard.food.detail.nutrient.FoodNutrient.ENERGY
import com.faltenreich.diaguard.food.detail.nutrient.FoodNutrient.FAT
import com.faltenreich.diaguard.food.detail.nutrient.FoodNutrient.FAT_SATURATED
import com.faltenreich.diaguard.food.detail.nutrient.FoodNutrient.FIBER
import com.faltenreich.diaguard.food.detail.nutrient.FoodNutrient.PROTEINS
import com.faltenreich.diaguard.food.detail.nutrient.FoodNutrient.SALT
import com.faltenreich.diaguard.food.detail.nutrient.FoodNutrient.SODIUM
import com.faltenreich.diaguard.food.detail.nutrient.FoodNutrient.SUGAR
import com.faltenreich.diaguard.shared.architecture.ViewModel

class FoodNutrientListViewModel(food: Food) : ViewModel() {

    private val nutrients = listOf(
        CARBOHYDRATES,
        SUGAR,
        ENERGY,
        FAT,
        FAT_SATURATED,
        FIBER,
        PROTEINS,
        SALT,
        SODIUM,
    )

    val data = nutrients.map { nutrient ->
        FoodNutrientData(
            nutrient = nutrient,
            per100g = nutrient.fromFood(food),
        )
    }
}