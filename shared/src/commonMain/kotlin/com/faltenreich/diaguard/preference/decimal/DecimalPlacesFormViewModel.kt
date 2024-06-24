package com.faltenreich.diaguard.preference.decimal

import com.faltenreich.diaguard.preference.DecimalPlaces
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class DecimalPlacesFormViewModel(
    getPreference: GetPreferenceUseCase,
    private val setPreference: SetPreferenceUseCase,
    private val illustrateDecimalPlaces: IllustrateDecimalPlacesUseCase,
) : ViewModel<DecimalPlacesFormState, DecimalPlacesFormIntent, Unit>() {

    private val decimalPlaces = getPreference(DecimalPlaces)
    private val illustration = decimalPlaces.map(illustrateDecimalPlaces::invoke)

    override val state = combine(
        decimalPlaces,
        illustration,
        ::DecimalPlacesFormState,
    )

    override suspend fun handleIntent(intent: DecimalPlacesFormIntent) {
        when (intent) {
            is DecimalPlacesFormIntent.SetDecimalPlaces -> setPreference(DecimalPlaces, intent.decimalPlaces)
        }
    }
}