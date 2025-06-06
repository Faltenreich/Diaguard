package com.faltenreich.diaguard.preference.overview

import com.faltenreich.diaguard.preference.color.ColorScheme
import com.faltenreich.diaguard.preference.screen.StartScreen

data class OverviewPreferenceListState(
    val colorScheme: ColorScheme,
    val startScreen: StartScreen,
    val decimalPlaces: DecimalPlaces,
    val appVersion: String,
) {

    data class DecimalPlaces(
        val decimalPlaces: Int,
        val illustration: String,
        val enableDecreaseButton: Boolean,
        val enableIncreaseButton: Boolean,
    )
}