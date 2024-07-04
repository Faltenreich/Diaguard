package com.faltenreich.diaguard.preference.decimal

import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.ShowSnackbarUseCase
import com.faltenreich.diaguard.preference.DecimalPlaces
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.validation.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DecimalPlacesFormViewModel(
    getPreference: GetPreferenceUseCase,
    private val setPreference: SetPreferenceUseCase,
    private val illustrateDecimalPlaces: IllustrateDecimalPlacesUseCase,
    private val validateDecimalPlaces: ValidateDecimalPlacesUseCase,
    private val showSnackbar: ShowSnackbarUseCase,
    private val closeModal: CloseModalUseCase,
) : ViewModel<DecimalPlacesFormState, DecimalPlacesFormIntent, Unit>() {

    private var decimalPlaces = MutableStateFlow(0)
    private val illustration = decimalPlaces.map(illustrateDecimalPlaces::invoke)

    override val state = combine(
        decimalPlaces,
        illustration,
        ::DecimalPlacesFormState,
    )

    init {
        scope.launch {
            getPreference(DecimalPlaces).collectLatest { decimalPlaces.value = it }
        }
    }

    override suspend fun handleIntent(intent: DecimalPlacesFormIntent) {
        when (intent) {
            // FIXME: Does not work on re-opening
            is DecimalPlacesFormIntent.Update -> updateIfValid(intent.decimalPlaces)
            is DecimalPlacesFormIntent.Confirm -> {
                setPreference(DecimalPlaces, decimalPlaces.value)
                // FIXME: Does not close modal
                closeModal()
            }
        }
    }

    private suspend fun updateIfValid(decimalPlaces: Int) {
        when (val result = validateDecimalPlaces(decimalPlaces)) {
            is ValidationResult.Success -> this.decimalPlaces.value = decimalPlaces
            is ValidationResult.Failure -> showSnackbar(result.error)
        }
    }
}