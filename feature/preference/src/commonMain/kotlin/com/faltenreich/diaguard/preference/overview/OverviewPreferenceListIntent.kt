package com.faltenreich.diaguard.preference.overview

import com.faltenreich.diaguard.navigation.NavigationTarget
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.preference.color.ColorScheme
import com.faltenreich.diaguard.preference.screen.StartScreen

sealed interface OverviewPreferenceListIntent {

    data class SetColorScheme(val colorScheme: ColorScheme) : OverviewPreferenceListIntent

    data class SetStartScreen(val startScreen: StartScreen) : OverviewPreferenceListIntent

    data class SetDecimalPlaces(val decimalPlaces: Int) : OverviewPreferenceListIntent

    data class PushScreen(val screen: Screen) : OverviewPreferenceListIntent

    data class NavigateTo(val target: NavigationTarget) : OverviewPreferenceListIntent

    data class OpenUrl(val url: String) : OverviewPreferenceListIntent

    data object OpenNotificationSettings : OverviewPreferenceListIntent
}