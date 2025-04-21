package com.faltenreich.diaguard.preference.overview

import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.navigation.system.OpenNotificationSettingsUseCase
import com.faltenreich.diaguard.navigation.system.OpenUrlUseCase
import com.faltenreich.diaguard.preference.color.ColorSchemePreference
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.screen.StartScreenPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.config.GetAppVersionUseCase
import kotlinx.coroutines.flow.combine

class OverviewPreferenceListViewModel(
    getPreference: GetPreferenceUseCase,
    getAppVersion: GetAppVersionUseCase,
    private val pushScreen: PushScreenUseCase,
    private val openUrl: OpenUrlUseCase,
    private val openNotificationSettings: OpenNotificationSettingsUseCase,
) : ViewModel<OverviewPreferenceListState, OverviewPreferenceListIntent, Unit>() {

    override val state = combine(
        getPreference(ColorSchemePreference),
        getPreference(StartScreenPreference),
        getPreference(DecimalPlacesPreference),
        getAppVersion(),
        ::OverviewPreferenceListState,
    )

    override suspend fun handleIntent(intent: OverviewPreferenceListIntent) {
        when (intent) {
            is OverviewPreferenceListIntent.PushScreen -> pushScreen(intent.screen)
            is OverviewPreferenceListIntent.OpenUrl -> openUrl(intent.url)
            is OverviewPreferenceListIntent.OpenNotificationSettings -> openNotificationSettings()
        }
    }
}