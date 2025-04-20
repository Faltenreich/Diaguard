package com.faltenreich.diaguard.preference.overview

import com.faltenreich.diaguard.preference.color.ColorScheme
import com.faltenreich.diaguard.preference.list.item.PreferenceListItem
import com.faltenreich.diaguard.preference.screen.StartScreen

data class OverviewPreferenceListState(
    val items: List<PreferenceListItem>,
    val colorScheme: ColorScheme,
    val startScreen: StartScreen,
    val decimalPlaces: Int,
    val appVersion: String,
)