package com.faltenreich.diaguard.preference.food

sealed interface FoodPreferenceListIntent {

    data class SetShowCustomFood(val showCustomFood: Boolean) : FoodPreferenceListIntent

    data class SetShowCommonFood(val showCommonFood: Boolean) : FoodPreferenceListIntent

    data class SetShowBrandedFood(val showBrandedFood: Boolean) : FoodPreferenceListIntent

    data class OpenUrl(val url: String) : FoodPreferenceListIntent
}