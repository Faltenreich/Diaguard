package com.faltenreich.diaguard.preference.screen

import com.faltenreich.diaguard.preference.StartScreen
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.map

class StartScreenFormViewModel(
    getPreference: GetPreferenceUseCase,
    private val setPreference: SetPreferenceUseCase,
) : ViewModel<StartScreenFormState, StartScreenFormIntent, Unit>() {

    override val state = getPreference(StartScreen.Preference).map(::StartScreenFormState)

    override suspend fun handleIntent(intent: StartScreenFormIntent) = with(intent) {
        when (this) {
            is StartScreenFormIntent.Select -> setPreference(StartScreen.Preference, startScreen)
        }
    }
}