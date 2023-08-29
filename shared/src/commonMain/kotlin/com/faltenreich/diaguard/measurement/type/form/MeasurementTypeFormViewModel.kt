package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MeasurementTypeFormViewModel(
    measurementTypeId: Long,
    getMeasurementTypeUseCase: GetMeasurementTypeUseCase = inject(),
    getMeasurementUnits: GetMeasurementUnitsUseCase = inject(),
    countMeasurementValuesOfType: CountMeasurementValuesOfTypeUseCase = inject(),
    private val updateMeasurementType: UpdateMeasurementTypeUseCase = inject(),
    private val deleteMeasurementType: DeleteMeasurementTypeUseCase = inject(),
) : ViewModel() {

    var name = MutableStateFlow("")

    private val showDeletionDialog = MutableStateFlow(false)

    private val type = getMeasurementTypeUseCase(measurementTypeId)

    // TODO: Merge showDeletionDialog and countMeasurementValuesOfType into
    private val state = type.flatMapLatest { type ->
        when (type) {
            null -> flowOf(MeasurementTypeFormViewState.Error)
            else -> getMeasurementUnits(type).map { units ->
                MeasurementTypeFormViewState.Loaded(type, units, showDeletionDialog = false, measurementCount = 0)
            }
        }
    }
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MeasurementTypeFormViewState.Loading
    )

    init {
        viewModelScope.launch {
            type.filterNotNull().collectLatest { type -> name.value = type.name }
        }
        // FIXME: Setting other flow at the same time cancels the first collector
        viewModelScope.launch {
            name.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { name ->
                val type = (viewState.value as? MeasurementTypeFormViewState.Loaded)?.type
                if (type != null) {
                    updateMeasurementType(type.copy(name = name))
                }
            }
        }
    }

    fun deleteTypeIfConfirmed() {
        showDeletionDialog.value = true
    }

    fun hideDeletionDialog() {
        showDeletionDialog.value = false
    }

    fun deleteType(type: MeasurementType) {
        deleteMeasurementType(type.id)
    }
}