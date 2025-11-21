package com.faltenreich.diaguard.preference.overview

import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.system.settings.OpenNotificationSettingsUseCase
import com.faltenreich.diaguard.system.web.OpenUrlUseCase
import com.faltenreich.diaguard.preference.color.ColorSchemePreference
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.decimal.IllustrateDecimalPlacesUseCase
import com.faltenreich.diaguard.preference.screen.StartScreenPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import com.faltenreich.diaguard.preference.version.GetAppVersionUseCase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class OverviewPreferenceListViewModel(
    getPreference: GetPreferenceUseCase,
    getAppVersion: GetAppVersionUseCase,
    private val illustrateDecimalPlaces: IllustrateDecimalPlacesUseCase,
    private val setPreference: SetPreferenceUseCase,
    private val pushScreen: PushScreenUseCase,
    private val openUrl: OpenUrlUseCase,
    private val openNotificationSettings: OpenNotificationSettingsUseCase,
) : ViewModel<OverviewPreferenceListState, OverviewPreferenceListIntent, Unit>() {

    private val decimalPlaces = getPreference(DecimalPlacesPreference)
    private val illustration = decimalPlaces.map(illustrateDecimalPlaces::invoke)
    private val enableDecreaseButton = decimalPlaces.map { it > decimalPlacesRange.first }
    private val enableIncreaseButton = decimalPlaces.map { it < decimalPlacesRange.last }

    override val state = combine(
        getAppVersion(),
        getPreference(ColorSchemePreference),
        combine(
            decimalPlaces,
            illustration,
            enableDecreaseButton,
            enableIncreaseButton,
            OverviewPreferenceListState::DecimalPlaces,
        ),
        getPreference(StartScreenPreference),
        ::OverviewPreferenceListState,
    )

    override suspend fun handleIntent(intent: OverviewPreferenceListIntent) = with(intent) {
        when (this) {
            is OverviewPreferenceListIntent.SetColorScheme -> setPreference(ColorSchemePreference, colorScheme)
            is OverviewPreferenceListIntent.SetStartScreen -> setPreference(StartScreenPreference, startScreen)
            is OverviewPreferenceListIntent.SetDecimalPlaces -> setDecimalPlacesIfValid(decimalPlaces)
            is OverviewPreferenceListIntent.PushScreen -> pushScreen(screen)
            is OverviewPreferenceListIntent.OpenUrl -> openUrl(url)
            is OverviewPreferenceListIntent.OpenNotificationSettings -> openNotificationSettings()
        }
    }

    private suspend fun setDecimalPlacesIfValid(decimalPlaces: Int) {
        if (decimalPlaces in decimalPlacesRange) {
            setPreference(DecimalPlacesPreference, decimalPlaces)
        }
    }

    companion object {

        private val decimalPlacesRange = 0 .. 3
    }
}