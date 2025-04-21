package com.faltenreich.diaguard.preference.overview

sealed interface OverviewPreferenceListIntent {

    data object OpenNotificationSettings : OverviewPreferenceListIntent
}