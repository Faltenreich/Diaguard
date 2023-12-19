package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class MeasurementTypeFormViewModel(
    measurementTypeId: Long,
    getMeasurementTypeUseCase: GetMeasurementTypeUseCase = inject(),
    countMeasurementValuesOfType: CountMeasurementValuesOfTypeUseCase = inject(),
    private val updateMeasurementType: UpdateMeasurementTypeUseCase = inject(),
    private val deleteMeasurementType: DeleteMeasurementTypeUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
) : ViewModel<MeasurementTypeFormViewState, MeasurementTypeFormIntent>() {

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
    }

    init {
        scope.launch {
            type.filterNotNull().distinctUntilChangedBy(MeasurementType::id).collectLatest { type ->
                typeName.value = type.name
                unitName.value = type.selectedUnit.name
            }
        }
        // FIXME: Setting other flow at the same time cancels the first collector
        scope.launch {
            typeName.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { name ->
                val type = (stateInScope.value as? MeasurementTypeFormViewState.Loaded)?.type
                checkNotNull(type)
                updateMeasurementType(type.copy(name = name))
            }
        }
        scope.launch {
            unitName.debounce(DateTimeConstants.INPUT_DEBOUNCE).collectLatest { name ->
                val type = (stateInScope.value as? MeasurementTypeFormViewState.Loaded)?.type
                checkNotNull(type)
                val unit = type.selectedUnit
                // FIXME: Wrangles units
                //  updateMeasurementUnit(unit.copy(name = name))
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