package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.unit.MeasurementTypeUnit
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MeasurementTypeFormViewModel(
    type: MeasurementType,
    getMeasurementTypeUseCase: GetMeasurementTypeUseCase = inject(),
    setMeasurementTypeName: SetMeasurementTypeNameUseCase = inject(),
    getMeasurementTypeUnits: GetMeasurementTypeUnitsUseCase = inject(),
    private val setSelectedMeasurementTypeUnit: SetSelectedMeasurementTypeUnitUseCase = inject(),
    private val deleteMeasurementType: DeleteMeasurementTypeUseCase = inject(),
) : ViewModel() {

    var name = MutableStateFlow(type.name)

    private val state = getMeasurementTypeUseCase(type).flatMapLatest { type ->
        getMeasurementTypeUnits(type!!).map { typeUnits ->
            MeasurementTypeFormViewState.Loaded(type, typeUnits)
        }
    }
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MeasurementTypeFormViewState.Loading(type)
    )

    init {
        // FIXME: Setting other flow at the same time cancels the first collector
        viewModelScope.launch {
            name.debounce(DateTimeConstants.INPUT_DEBOUNCE)
                .collectLatest { name -> setMeasurementTypeName(type, name = name) }
        }
    }

    fun deleteType(type: MeasurementType) {
        deleteMeasurementType(type)
    }

    fun selectTypeUnit(typeUnit: MeasurementTypeUnit) {
        setSelectedMeasurementTypeUnit(
            type = viewState.value.type,
            typeUnit = typeUnit,
        )
    }
}