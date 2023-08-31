package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.form.UpdateMeasurementTypeUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MeasurementPropertyFormViewModel(
    property: MeasurementProperty,
    getMeasurementTypesUseCase: GetMeasurementTypesUseCase = inject(),
    countMeasurementValuesOfProperty: CountMeasurementValuesOfPropertyUseCase = inject(),
    updateMeasurementProperty: UpdateMeasurementPropertyUseCase = inject(),
    private val createMeasurementType: CreateMeasurementTypeUseCase = inject(),
    private val updateMeasurementType: UpdateMeasurementTypeUseCase = inject(),
    private val deleteMeasurementProperty: DeleteMeasurementPropertyUseCase = inject(),
) : ViewModel() {

    var name = MutableStateFlow(property.name)
    var icon = MutableStateFlow(property.icon ?: "")

    private val showFormDialog = MutableStateFlow(false)
    private val showDeletionDialog = MutableStateFlow(false)

    private val state = combine(
        showFormDialog,
        showDeletionDialog,
        getMeasurementTypesUseCase(property),
        countMeasurementValuesOfProperty(property),
    ) { showFormDialog, showDeletionDialog, types, measurementCount ->
        MeasurementPropertyFormViewState.Loaded(
            property = property,
            showFormDialog = showFormDialog,
            showDeletionDialog = showDeletionDialog,
            types = types,
            measurementCount = measurementCount,
        )
    }
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MeasurementPropertyFormViewState.Loading(property),
    )

    val types: List<MeasurementType>?
        get() = (viewState.value as? MeasurementPropertyFormViewState.Loaded)?.types

    init {
        // FIXME: Setting both at the same time cancels the first collector
        viewModelScope.launch {
            name.debounce(DateTimeConstants.INPUT_DEBOUNCE)
                .collectLatest { name -> updateMeasurementProperty(property.copy(name = name)) }
        }
        viewModelScope.launch {
            icon.debounce(DateTimeConstants.INPUT_DEBOUNCE)
                .collectLatest { icon -> updateMeasurementProperty(property.copy(icon = icon)) }
        }
    }

    fun decrementSortIndex(type: MeasurementType) {
        val types = types ?: return
        swapSortIndexes(first = type, second = types.last { it.sortIndex < type.sortIndex })
    }

    fun incrementSortIndex(type: MeasurementType) {
        val types = types ?: return
        swapSortIndexes(first = type, second = types.first { it.sortIndex > type.sortIndex })
    }

    private fun swapSortIndexes(
        first: MeasurementType,
        second: MeasurementType,
    ) {
        updateMeasurementType(first.copy(sortIndex = second.sortIndex))
        updateMeasurementType(second.copy(sortIndex = first.sortIndex))
    }

    fun showFormDialog() {
        showFormDialog.value = true
    }

    fun hideFormDialog() {
        showFormDialog.value = false
    }

    fun createType(
        typeName: String,
        unitName: String,
        types: List<MeasurementType>,
    ) {
        createMeasurementType(
            typeName = typeName,
            typeSortIndex = types.maxOfOrNull(MeasurementType::sortIndex)?.plus(1) ?: 0,
            propertyId = viewState.value.property.id,
            unitName = unitName,
        )
    }

    fun deletePropertyIfConfirmed() {
        showDeletionDialog.value = true
    }

    fun hideDeletionDialog() {
        showDeletionDialog.value = false
    }

    fun deleteProperty(property: MeasurementProperty) {
        deleteMeasurementProperty(property)
    }
}