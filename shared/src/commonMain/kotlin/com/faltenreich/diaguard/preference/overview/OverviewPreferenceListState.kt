package com.faltenreich.diaguard.preference.overview

data class OverviewPreferenceListState(
    val appVersion: String,
    val colorScheme: ColorScheme,
    val decimalPlaces: DecimalPlaces,
    val startScreen: StartScreen,
) {

    data class ColorScheme(
        val selection: com.faltenreich.diaguard.preference.color.ColorScheme,
    )

    data class DecimalPlaces(
        val selection: Int,
        val illustration: String,
        val enableDecreaseButton: Boolean,
        val enableIncreaseButton: Boolean,
    )

    data class StartScreen(
        val selection: com.faltenreich.diaguard.preference.screen.StartScreen,
    )
}