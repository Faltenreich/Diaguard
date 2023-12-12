package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.architecture.combine
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MeasurementPropertyFormViewModel(
    property: MeasurementProperty,
    private val dispatcher: CoroutineDispatcher = inject(),
    getMeasurementTypesUseCase: GetMeasurementTypesUseCase = inject(),
    countMeasurementValuesOfProperty: CountMeasurementValuesOfPropertyUseCase = inject(),
    updateMeasurementProperty: UpdateMeasurementPropertyUseCase = inject(),
    private val createMeasurementType: CreateMeasurementTypeUseCase = inject(),
    private val deleteMeasurementProperty: DeleteMeasurementPropertyUseCase = inject(),
) : ViewModel {

    var name = MutableStateFlow(property.name)
    var icon = MutableStateFlow(property.icon ?: "")

    private val showIconPicker = MutableStateFlow(false)
    private val showFormDialog = MutableStateFlow(false)
    private val showDeletionDialog = MutableStateFlow(false)

    private val state = combine(
        flowOf(property),
        showIconPicker,
        showFormDialog,
        showDeletionDialog,
        getMeasurementTypesUseCase(property),
        countMeasurementValuesOfProperty(property),
        MeasurementPropertyFormViewState::Loaded,
    ).flowOn(dispatcher)
    val viewState = state.stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = MeasurementPropertyFormViewState.Loading(property),
    )

    init {
        // FIXME: Setting both at the same time cancels the first collector
        scope.launch(dispatcher) {
            name.debounce(DateTimeConstants.INPUT_DEBOUNCE)
                .collectLatest { name -> updateMeasurementProperty(property.copy(name = name)) }
        }
        scope.launch(dispatcher) {
            icon.debounce(DateTimeConstants.INPUT_DEBOUNCE)
                .collectLatest { icon -> updateMeasurementProperty(property.copy(icon = icon)) }
        }
    }

    fun showIconPicker() = scope.launch(dispatcher) {
        showIconPicker.value = true
    }

    fun hideIconPicker() = scope.launch(dispatcher) {
        showIconPicker.value = false
    }

    fun showFormDialog() = scope.launch(dispatcher) {
        showFormDialog.value = true
    }

    fun hideFormDialog() = scope.launch (dispatcher) {
        showFormDialog.value = false
    }

    fun createType(
        typeName: String,
        unitName: String,
        types: List<MeasurementType>,
    ) = scope.launch(dispatcher) {
        createMeasurementType(
            typeKey = null,
            typeName = typeName,
            typeSortIndex = types.maxOfOrNull(MeasurementType::sortIndex)?.plus(1) ?: 0,
            propertyId = viewState.value.property.id,
            unitKey = null,
            unitName = unitName,
        )
    }

    fun deletePropertyIfConfirmed() = scope.launch(dispatcher) {
        showDeletionDialog.value = true
    }

    fun hideDeletionDialog() = scope.launch(dispatcher) {
        showDeletionDialog.value = false
    }

    fun deleteProperty(property: MeasurementProperty) = scope.launch(dispatcher) {
        deleteMeasurementProperty(property)
    }
}