package com.faltenreich.diaguard.preference.decimal

import com.faltenreich.diaguard.navigation.ShowSnackbarUseCase
import com.faltenreich.diaguard.preference.DecimalPlaces
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.validation.ValidationResult
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class DecimalPlacesFormViewModel(
    getPreference: GetPreferenceUseCase,
    private val setPreference: SetPreferenceUseCase,
    private val illustrateDecimalPlaces: IllustrateDecimalPlacesUseCase,
    private val validateDecimalPlaces: ValidateDecimalPlacesUseCase,
    private val showSnackbar: ShowSnackbarUseCase,
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
            is DecimalPlacesFormIntent.Update -> updateIfValid(intent.decimalPlaces)
        }
    }

    private suspend fun updateIfValid(decimalPlaces: Int) {
        when (val result = validateDecimalPlaces(decimalPlaces)) {
            is ValidationResult.Success -> setPreference(DecimalPlaces, decimalPlaces)
            // FIXME: Hidden by BottomSheet
            is ValidationResult.Failure -> showSnackbar(result.error)
        }
    }
}