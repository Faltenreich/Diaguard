package com.faltenreich.diaguard.preference.food

import com.faltenreich.diaguard.preference.list.item.PreferenceListItem

data class FoodPreferenceListState(
    val preferences: List<PreferenceListItem>,
    val showAnyFood: Boolean,
)