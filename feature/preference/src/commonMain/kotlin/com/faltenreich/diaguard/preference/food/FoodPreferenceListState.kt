package com.faltenreich.diaguard.preference.food

internal data class FoodPreferenceListState(
    val showCustomFood: Boolean,
    val showCommonFood: Boolean,
    val showBrandedFood: Boolean,
) {

    val showAnyFood: Boolean = showBrandedFood || showCommonFood || showCustomFood
}