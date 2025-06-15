package com.faltenreich.diaguard.preference.overview

import com.faltenreich.diaguard.preference.color.ColorScheme
import com.faltenreich.diaguard.preference.screen.StartScreen

data class OverviewPreferenceListState(
    val appVersion: String,
    val colorScheme: ColorScheme,
    val decimalPlaces: DecimalPlaces,
    val startScreen: StartScreen,
) {

    data class DecimalPlaces(
        val selection: Int,
        val illustration: String,
        val enableDecreaseButton: Boolean,
        val enableIncreaseButton: Boolean,
    )
}