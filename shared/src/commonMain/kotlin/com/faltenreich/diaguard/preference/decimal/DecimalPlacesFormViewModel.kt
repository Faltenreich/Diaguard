package com.faltenreich.diaguard.preference.decimal

import com.faltenreich.diaguard.preference.DecimalPlaces
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.map

class DecimalPlacesFormViewModel(
    getPreference: GetPreferenceUseCase,
    private val setPreference: SetPreferenceUseCase,
) : ViewModel<DecimalPlacesFormState, DecimalPlacesFormIntent, Unit>() {

    private val decimalPlaces = getPreference(DecimalPlaces)

    override val state = decimalPlaces.map(::DecimalPlacesFormState)

    override suspend fun handleIntent(intent: DecimalPlacesFormIntent) {
        when (intent) {
            is DecimalPlacesFormIntent.SetDecimalPlaces -> setPreference(DecimalPlaces, intent.decimalPlaces)
        }
    }
}