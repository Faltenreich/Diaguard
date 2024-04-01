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
    measurementType: MeasurementType,
    getMeasurementTypeUseCase: GetMeasurementTypeUseCase = inject(),
    countMeasurementValuesOfType: CountMeasurementValuesOfTypeUseCase = inject(),
    private val updateMeasurementType: UpdateMeasurementTypeUseCase = inject(),
    private val deleteMeasurementType: DeleteMeasurementTypeUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
) : ViewModel<MeasurementTypeFormViewState, MeasurementTypeFormIntent>() {

    var typeName = MutableStateFlow(measurementType.name)
    var unitName = MutableStateFlow(measurementType.selectedUnit.name)
    var lowValue = MutableStateFlow(measurementType.lowValue?.toString() ?: "")
    var targetValue = MutableStateFlow(measurementType.targetValue?.toString() ?: "")
    var highValue = MutableStateFlow(measurementType.highValue?.toString() ?: "")

    private val showDeletionDialog = MutableStateFlow(false)

    override val state = combine(
        getMeasurementTypeUseCase(measurementType),
        showDeletionDialog,
        countMeasurementValuesOfType(measurementType),
    ) { type, showDeletionDialog, measurementCount ->
        when (type) {
            null -> MeasurementTypeFormViewState.Error
            else ->  MeasurementTypeFormViewState.Loaded(
                type = type,
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
                val type = (stateInScope.value as? MeasurementTypeFormViewState.Loaded)?.type
                checkNotNull(type)
                updateMeasurementType(type.copy(name = input))
            }
        }
        scope.launch {
            unitName.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { input ->
                val type = (stateInScope.value as? MeasurementTypeFormViewState.Loaded)?.type
                checkNotNull(type)
                // FIXME: Wrangles units
                //  updateMeasurementUnit(unit.copy(name = name))
            }
        }
        scope.launch {
            lowValue.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { input ->
                val type = (stateInScope.value as? MeasurementTypeFormViewState.Loaded)?.type
                checkNotNull(type)
                updateMeasurementType(type.copy(lowValue = input.toDoubleOrNull()))
            }
        }
        scope.launch {
            targetValue.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { input ->
                val type = (stateInScope.value as? MeasurementTypeFormViewState.Loaded)?.type
                checkNotNull(type)
                updateMeasurementType(type.copy(targetValue = input.toDoubleOrNull()))
            }
        }
        scope.launch {
            highValue.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { input ->
                val type = (stateInScope.value as? MeasurementTypeFormViewState.Loaded)?.type
                checkNotNull(type)
                updateMeasurementType(type.copy(highValue = input.toDoubleOrNull()))
            }
        }
    }

    override fun onIntent(intent: MeasurementTypeFormIntent) {
        when (intent) {
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