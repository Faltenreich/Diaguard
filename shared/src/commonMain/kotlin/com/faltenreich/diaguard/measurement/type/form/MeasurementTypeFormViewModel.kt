package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.datetime.factory.DateTimeConstants
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class MeasurementTypeFormViewModel(
    private val type: MeasurementType,
    getMeasurementTypeUseCase: GetMeasurementTypeUseCase = inject(),
    countMeasurementValuesOfType: CountMeasurementValuesOfTypeUseCase = inject(),
    private val updateMeasurementType: UpdateMeasurementTypeUseCase = inject(),
    private val deleteMeasurementType: DeleteMeasurementTypeUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
) : ViewModel<MeasurementTypeFormViewState, MeasurementTypeFormIntent>() {

    var typeName = MutableStateFlow(type.name)
    var unitName = MutableStateFlow(type.selectedUnit.name)
    var valueRangeMinimum = MutableStateFlow(type.range.minimum.toString())
    var valueRangeLow = MutableStateFlow(type.range.low?.toString() ?: "")
    var valueRangeTarget = MutableStateFlow(type.range.target?.toString() ?: "")
    var valueRangeHigh = MutableStateFlow(type.range.high?.toString() ?: "")
    var valueRangeMaximum = MutableStateFlow(type.range.maximum.toString())
    var isValueRangeHighlighted = MutableStateFlow(type.range.isHighlighted)

    private val showDeletionDialog = MutableStateFlow(false)

    override val state = combine(
        getMeasurementTypeUseCase(type),
        unitName,
        showDeletionDialog,
        countMeasurementValuesOfType(type),
    ) { type, unitName, showDeletionDialog, measurementCount ->
        when (type) {
            null -> MeasurementTypeFormViewState.Error
            else ->  MeasurementTypeFormViewState.Loaded(
                type = type,
                unitName = if (type.isUserGenerated) unitName else type.selectedUnit.abbreviation,
                showDeletionDialog = showDeletionDialog,
                measurementCount = measurementCount,
            )
        }
    }

    init {
        // FIXME: Setting other flow at the same time cancels the first collector
        // TODO: Format values accordingly, using MeasurementValueForDatabase/-User
        // TODO: Validate input
        scope.launch {
            typeName.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { input ->
                updateMeasurementType(type.copy(name = input))
            }
        }
        scope.launch {
            unitName.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { input ->
                // FIXME: Wrangles units
                //  updateMeasurementUnit(unit.copy(name = name))
            }
        }
        scope.launch {
            valueRangeMinimum.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { input ->
                updateMeasurementType(type.copy(range = type.range.copy(minimum = input.toDoubleOrNull() ?: 0.0)))
            }
        }
        scope.launch {
            valueRangeLow.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { input ->
                updateMeasurementType(type.copy(range = type.range.copy(low = input.toDoubleOrNull())))
            }
        }
        scope.launch {
            valueRangeTarget.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { input ->
                updateMeasurementType(type.copy(range = type.range.copy(target = input.toDoubleOrNull())))
            }
        }
        scope.launch {
            valueRangeHigh.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { input ->
                updateMeasurementType(type.copy(range = type.range.copy(high = input.toDoubleOrNull())))
            }
        }
        scope.launch {
            valueRangeMaximum.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { input ->
                updateMeasurementType(type.copy(range = type.range.copy(maximum = input.toDoubleOrNull() ?: 0.0)))
            }
        }
        scope.launch {
            isValueRangeHighlighted.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { input ->
                updateMeasurementType(type.copy(range = type.range.copy(isHighlighted = input)))
            }
        }
    }

    override fun onIntent(intent: MeasurementTypeFormIntent) {
        when (intent) {
            // TODO: Replace with Modal
            is MeasurementTypeFormIntent.ShowDeletionDialog -> showDeletionDialog.value = true
            is MeasurementTypeFormIntent.HideDeletionDialog -> showDeletionDialog.value = false
            is MeasurementTypeFormIntent.DeleteType -> {
                deleteMeasurementType(intent.type.id)
                showDeletionDialog.value = false
                navigateBack()
            }
        }
    }
}