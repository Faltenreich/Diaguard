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
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class MeasurementTypeFormViewModel(
    measurementType: MeasurementType,
    getMeasurementTypeUseCase: GetMeasurementTypeUseCase = inject(),
    countMeasurementValuesOfType: CountMeasurementValuesOfTypeUseCase = inject(),
    private val updateMeasurementType: UpdateMeasurementTypeUseCase = inject(),
    private val deleteMeasurementType: DeleteMeasurementTypeUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
) : ViewModel<MeasurementTypeFormViewState, MeasurementTypeFormIntent>() {

    var typeName = MutableStateFlow("")
    var unitName = MutableStateFlow("")
    var minimumValue = MutableStateFlow("")
    var lowValue = MutableStateFlow("")
    var targetValue = MutableStateFlow("")
    var highValue = MutableStateFlow("")
    var maximumValue = MutableStateFlow("")

    private val showDeletionDialog = MutableStateFlow(false)

    private val type = getMeasurementTypeUseCase(measurementType)

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
        scope.launch {
            type.filterNotNull().distinctUntilChangedBy(MeasurementType::id).collectLatest { type ->
                typeName.value = type.name
                unitName.value = type.selectedUnit.name
                minimumValue.value = type.minimumValue.toString()
                lowValue.value = type.lowValue?.toString() ?: ""
                targetValue.value = type.targetValue?.toString() ?: ""
                highValue.value = type.highValue?.toString() ?: ""
                maximumValue.value = type.maximumValue.toString()
            }
        }
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
            minimumValue.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { input ->
                val type = (stateInScope.value as? MeasurementTypeFormViewState.Loaded)?.type
                checkNotNull(type)
                updateMeasurementType(type.copy(minimumValue = input.toDoubleOrNull() ?: 0.0))
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
        scope.launch {
            maximumValue.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { input ->
                val type = (stateInScope.value as? MeasurementTypeFormViewState.Loaded)?.type
                checkNotNull(type)
                updateMeasurementType(type.copy(maximumValue = input.toDoubleOrNull() ?: 0.0))
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