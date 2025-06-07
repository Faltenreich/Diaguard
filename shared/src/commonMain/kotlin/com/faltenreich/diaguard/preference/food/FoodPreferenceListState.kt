package com.faltenreich.diaguard.preference.food

data class FoodPreferenceListState(
    val showCustomFood: Boolean,
    val showCommonFood: Boolean,
    val showBrandedFood: Boolean,
) {

    val showAnyFood: Boolean = showBrandedFood || showCommonFood || showCustomFood
}