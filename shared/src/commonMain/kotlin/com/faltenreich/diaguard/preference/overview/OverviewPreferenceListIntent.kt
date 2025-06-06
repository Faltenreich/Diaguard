package com.faltenreich.diaguard.preference.overview

import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.preference.color.ColorScheme

sealed interface OverviewPreferenceListIntent {

    data class SetColorScheme(val colorScheme: ColorScheme) : OverviewPreferenceListIntent

    data class PushScreen(val screen: Screen) : OverviewPreferenceListIntent

    data class OpenUrl(val url: String) : OverviewPreferenceListIntent

    data object OpenNotificationSettings : OverviewPreferenceListIntent
}