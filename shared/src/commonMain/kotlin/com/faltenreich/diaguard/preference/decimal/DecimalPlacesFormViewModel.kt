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
    private val enableDecreaseButton = decimalPlaces.map { it > decimalPlacesRange.first }
    private val enableIncreaseButton = decimalPlaces.map { it < decimalPlacesRange.last }

    override val state = combine(
        decimalPlaces,
        illustration,
        enableDecreaseButton,
        enableIncreaseButton,
        ::DecimalPlacesFormState,
    )

    override suspend fun handleIntent(intent: DecimalPlacesFormIntent) {
        when (intent) {
            is DecimalPlacesFormIntent.Update -> updateIfValid(intent.decimalPlaces)
        }
    }

    private suspend fun updateIfValid(decimalPlaces: Int) {
        if (decimalPlaces in decimalPlacesRange) {
            setPreference(DecimalPlaces, decimalPlaces)
        }
    }

    companion object {

        private val decimalPlacesRange = 0 .. 3
    }
}