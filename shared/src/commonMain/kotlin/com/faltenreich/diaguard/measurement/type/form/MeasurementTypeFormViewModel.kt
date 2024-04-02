package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.datetime.factory.DateTimeConstants
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private var _typeName = MutableStateFlow(type.name)
    val typeName = _typeName.asStateFlow()

    private var _unitName = MutableStateFlow(type.selectedUnit.name)
    val unitName = _unitName.asStateFlow()

    private var _valueRangeMinimum = MutableStateFlow(type.range.minimum.toString())
    val valueRangeMinimum = _valueRangeMinimum.asStateFlow()

    private var _valueRangeLow = MutableStateFlow(type.range.low?.toString() ?: "")
    val valueRangeLow = _valueRangeLow.asStateFlow()

    private var _valueRangeTarget = MutableStateFlow(type.range.target?.toString() ?: "")
    val valueRangeTarget = _valueRangeTarget.asStateFlow()

    private var _valueRangeHigh = MutableStateFlow(type.range.high?.toString() ?: "")
    val valueRangeHigh = _valueRangeHigh.asStateFlow()

    private var _valueRangeMaximum = MutableStateFlow(type.range.maximum.toString())
    val valueRangeMaximum = _valueRangeMaximum.asStateFlow()

    private var _isValueRangeHighlighted = MutableStateFlow(type.range.isHighlighted)
    val isValueRangeHighlighted = _isValueRangeHighlighted.asStateFlow()

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
            _valueRangeMinimum.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { input ->
                updateMeasurementType(type.copy(range = type.range.copy(minimum = input.toDoubleOrNull() ?: 0.0)))
            }
        }
        scope.launch {
            _valueRangeLow.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { input ->
                updateMeasurementType(type.copy(range = type.range.copy(low = input.toDoubleOrNull())))
            }
        }
        scope.launch {
            _valueRangeTarget.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { input ->
                updateMeasurementType(type.copy(range = type.range.copy(target = input.toDoubleOrNull())))
            }
        }
        scope.launch {
            _valueRangeHigh.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { input ->
                updateMeasurementType(type.copy(range = type.range.copy(high = input.toDoubleOrNull())))
            }
        }
        scope.launch {
            _valueRangeMaximum.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { input ->
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
            is MeasurementTypeFormIntent.EditTypeName -> _typeName.value = intent.input
            is MeasurementTypeFormIntent.EditUnitName -> _unitName.value = intent.input
            is MeasurementTypeFormIntent.EditValueRangeMinimum -> _valueRangeMinimum.value = intent.input
            is MeasurementTypeFormIntent.EditValueRangeLow -> _valueRangeLow.value = intent.input
            is MeasurementTypeFormIntent.EditValueRangeTarget -> _valueRangeTarget.value = intent.input
            is MeasurementTypeFormIntent.EditValueRangeHigh -> _valueRangeHigh.value = intent.input
            is MeasurementTypeFormIntent.EditValueRangeMaximum -> _valueRangeMaximum.value = intent.input
            is MeasurementTypeFormIntent.EditIsValueRangeHighlighted -> _isValueRangeHighlighted.value = intent.input
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