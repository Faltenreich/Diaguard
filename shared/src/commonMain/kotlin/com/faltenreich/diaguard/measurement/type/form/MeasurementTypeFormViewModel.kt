package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MeasurementTypeFormViewModel(
    measurementTypeId: Long,
    private val dispatcher: CoroutineDispatcher = inject(),
    getMeasurementTypeUseCase: GetMeasurementTypeUseCase = inject(),
    countMeasurementValuesOfType: CountMeasurementValuesOfTypeUseCase = inject(),
    private val updateMeasurementType: UpdateMeasurementTypeUseCase = inject(),
    private val deleteMeasurementType: DeleteMeasurementTypeUseCase = inject(),
) : ViewModel<MeasurementTypeFormViewState>() {

    var typeName = MutableStateFlow("")
    var unitName = MutableStateFlow("")

    private val showDeletionDialog = MutableStateFlow(false)

    private val type = getMeasurementTypeUseCase(measurementTypeId)

    override val state = combine(
        getMeasurementTypeUseCase(measurementTypeId),
        showDeletionDialog,
        countMeasurementValuesOfType(measurementTypeId),
    ) { type, showDeletionDialog, measurementCount ->
        when (type) {
            null -> MeasurementTypeFormViewState.Error
            else ->  MeasurementTypeFormViewState.Loaded(
                type = type,
                showDeletionDialog = showDeletionDialog,
                measurementCount = measurementCount,
            )
        }
    }.flowOn(dispatcher)

    init {
        scope.launch(dispatcher) {
            type.filterNotNull().distinctUntilChangedBy(MeasurementType::id).collectLatest { type ->
                typeName.value = type.name
                unitName.value = type.selectedUnit.name
            }
        }
        // FIXME: Setting other flow at the same time cancels the first collector
        scope.launch(dispatcher) {
            typeName.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { name ->
                val type = (stateInScope.value as? MeasurementTypeFormViewState.Loaded)?.type
                checkNotNull(type)
                updateMeasurementType(type.copy(name = name))
            }
        }
        scope.launch(dispatcher) {
            unitName.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { name ->
                val type = (stateInScope.value as? MeasurementTypeFormViewState.Loaded)?.type
                checkNotNull(type)
                val unit = type.selectedUnit
                // FIXME: Wrangles units
                //  updateMeasurementUnit(unit.copy(name = name))
            }
        }
    }

    fun deleteTypeIfConfirmed() = scope.launch(dispatcher) {
        showDeletionDialog.value = true
    }

    fun hideDeletionDialog() = scope.launch(dispatcher) {
        showDeletionDialog.value = false
    }

    fun deleteType(type: MeasurementType) = scope.launch(dispatcher) {
        deleteMeasurementType(type.id)
    }
}