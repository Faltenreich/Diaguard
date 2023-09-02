package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MeasurementTypeFormViewModel(
    measurementTypeId: Long,
    private val dispatcher: CoroutineDispatcher = inject(),
    getMeasurementTypeUseCase: GetMeasurementTypeUseCase = inject(),
    countMeasurementValuesOfType: CountMeasurementValuesOfTypeUseCase = inject(),
    private val updateMeasurementType: UpdateMeasurementTypeUseCase = inject(),
    private val deleteMeasurementType: DeleteMeasurementTypeUseCase = inject(),
    private val updateMeasurementUnit: UpdateMeasurementUnitUseCase = inject(),
) : ViewModel() {

    var typeName = MutableStateFlow("")
    var unitName = MutableStateFlow("")

    private val showDeletionDialog = MutableStateFlow(false)

    private val type = getMeasurementTypeUseCase(measurementTypeId)

    // TODO: Merge showDeletionDialog and countMeasurementValuesOfType into
    private val state = type.map { type ->
        when (type) {
            null -> MeasurementTypeFormViewState.Error
            else ->  MeasurementTypeFormViewState.Loaded(type, showDeletionDialog = false, measurementCount = 0)
        }
    }.flowOn(dispatcher)
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MeasurementTypeFormViewState.Loading
    )

    init {
        viewModelScope.launch(dispatcher) {
            type.filterNotNull().distinctUntilChangedBy(MeasurementType::id).collectLatest { type ->
                typeName.value = type.name
                unitName.value = type.selectedUnit?.name ?: ""
            }
        }
        // FIXME: Setting other flow at the same time cancels the first collector
        viewModelScope.launch(dispatcher) {
            typeName.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { name ->
                val type = (viewState.value as? MeasurementTypeFormViewState.Loaded)?.type ?: throw IllegalStateException("Type must not be null at this point")
                updateMeasurementType(type.copy(name = name))
            }
        }
        viewModelScope.launch(dispatcher) {
            unitName.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { name ->
                val type = (viewState.value as? MeasurementTypeFormViewState.Loaded)?.type ?: throw IllegalStateException("Type must not be null at this point")
                val unit = type.selectedUnit ?: throw IllegalStateException("selectedUnitId of $type must not be null at this point")
                // FIXME: Wrangles units
                //  updateMeasurementUnit(unit.copy(name = name))
            }
        }
    }

    fun setSelectedUnit(unit: MeasurementUnit) = viewModelScope.launch(dispatcher) {
        val type = (viewState.value as? MeasurementTypeFormViewState.Loaded)?.type ?: return@launch
        updateMeasurementType(type.copy(selectedUnitId = unit.id))
    }

    fun deleteTypeIfConfirmed() = viewModelScope.launch(dispatcher) {
        showDeletionDialog.value = true
    }

    fun hideDeletionDialog() = viewModelScope.launch(dispatcher) {
        showDeletionDialog.value = false
    }

    fun deleteType(type: MeasurementType) = viewModelScope.launch(dispatcher) {
        deleteMeasurementType(type.id)
    }
}