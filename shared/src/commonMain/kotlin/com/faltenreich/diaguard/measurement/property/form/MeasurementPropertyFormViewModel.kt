package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MeasurementPropertyFormViewModel(
    property: MeasurementProperty,
    getMeasurementTypesUseCase: GetMeasurementTypesUseCase = inject(),
    setMeasurementPropertyName: SetMeasurementPropertyNameUseCase = inject(),
    setMeasurementPropertyIcon: SetMeasurementPropertyIconUseCase = inject(),
    private val setMeasurementTypeSortIndex: SetMeasurementTypeSortIndexUseCase = inject(),
) : ViewModel() {

    var name = MutableStateFlow(property.name)
    var icon = MutableStateFlow(property.icon ?: "")

    private val state = getMeasurementTypesUseCase(property).map { types ->
        MeasurementPropertyFormViewState.Loaded(property, types)
    }
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MeasurementPropertyFormViewState.Loading(property),
    )

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
        setSortIndex(type, sortIndex = type.sortIndex - 1)
    }

    fun incrementSortIndex(type: MeasurementType) {
        setSortIndex(type, sortIndex = type.sortIndex + 1)
    }

    private fun setSortIndex(type: MeasurementType, sortIndex: Long) {
        val types = (viewState.value as? MeasurementPropertyFormViewState.Loaded)?.types ?: return
        val isDecrementing = sortIndex < type.sortIndex

        setMeasurementTypeSortIndex(type = type, sortIndex = sortIndex)

        val replacement = types.first { it.sortIndex == sortIndex }
        val replacementSortIndex = if (isDecrementing) sortIndex + 1 else sortIndex -1
        setMeasurementTypeSortIndex(type = replacement, sortIndex = replacementSortIndex)
    }
}