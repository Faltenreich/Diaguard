package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType
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
    setMeasurementPropertyName: SetMeasurementPropertyNameUseCase = inject(),
    setMeasurementPropertyIcon: SetMeasurementPropertyIconUseCase = inject(),
    private val setMeasurementTypeSortIndex: SetMeasurementTypeSortIndexUseCase = inject(),
    private val createMeasurementType: CreateMeasurementTypeUseCase = inject(),
    private val deleteMeasurementProperty: DeleteMeasurementPropertyUseCase = inject(),
) : ViewModel() {

    var name = MutableStateFlow(property.name)
    var icon = MutableStateFlow(property.icon ?: "")

    private val showFormDialog = MutableStateFlow(false)

    private val state = combine(
        showFormDialog,
        getMeasurementTypesUseCase(property),
    ) { showFormDialog, types ->
        MeasurementPropertyFormViewState.Loaded(property, showFormDialog, types)
    }
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MeasurementPropertyFormViewState.Loading(property, showFormDialog = false),
    )

    val types = (viewState.value as? MeasurementPropertyFormViewState.Loaded)?.types

    init {
        // FIXME: Setting both at the same time cancels the first collector
        viewModelScope.launch {
            name.debounce(DateTimeConstants.INPUT_DEBOUNCE)
                .collectLatest { name -> setMeasurementPropertyName(property, name = name) }
        }
        viewModelScope.launch {
            icon.debounce(DateTimeConstants.INPUT_DEBOUNCE)
                .collectLatest { icon -> setMeasurementPropertyIcon(property, icon = icon) }
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
        val firstSortIndex = first.sortIndex
        val secondSortIndex = second.sortIndex
        setMeasurementTypeSortIndex(first, sortIndex = secondSortIndex)
        setMeasurementTypeSortIndex(second, sortIndex = firstSortIndex)
    }

    fun showFormDialog() {
        showFormDialog.value = true
    }

    fun hideFormDialog() {
        showFormDialog.value = false
    }

    fun createType(name: String) {
        val types = (viewState.value as? MeasurementPropertyFormViewState.Loaded)?.types ?: return
        createMeasurementType(
            name = name,
            sortIndex = types.maxOf(MeasurementType::sortIndex) + 1,
            propertyId = viewState.value.property.id,
        )
    }

    fun deleteProperty(property: MeasurementProperty) {
        // TODO: Confirm deletion
        deleteMeasurementProperty(property)
    }
}