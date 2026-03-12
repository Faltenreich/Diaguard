package com.faltenreich.diaguard.preference.overview

import com.faltenreich.diaguard.data.navigation.NavigationTarget
import com.faltenreich.diaguard.data.preference.color.ColorScheme
import com.faltenreich.diaguard.data.preference.startscreen.StartScreen

internal sealed interface OverviewPreferenceListIntent {

    data class SetColorScheme(val colorScheme: ColorScheme) : OverviewPreferenceListIntent

    data class SetStartScreen(val startScreen: StartScreen) : OverviewPreferenceListIntent

    data class SetDecimalPlaces(val decimalPlaces: Int) : OverviewPreferenceListIntent

    data class NavigateTo(val target: NavigationTarget) : OverviewPreferenceListIntent

    data class OpenUrl(val url: String) : OverviewPreferenceListIntent

    data object OpenNotificationSettings : OverviewPreferenceListIntent
}