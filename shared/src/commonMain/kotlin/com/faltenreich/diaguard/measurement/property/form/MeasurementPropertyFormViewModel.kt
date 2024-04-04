package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class MeasurementPropertyFormViewModel(
    val property: MeasurementProperty,
    getMeasurementTypesUseCase: GetMeasurementTypesUseCase = inject(),
    countMeasurementValuesOfProperty: CountMeasurementValuesOfPropertyUseCase = inject(),
    private val updateProperty: UpdateMeasurementPropertyUseCase = inject(),
    private val deleteProperty: DeleteMeasurementPropertyUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
) : ViewModel<MeasurementPropertyFormViewState, MeasurementPropertyFormIntent>() {

    var name = MutableStateFlow(property.name)
    var icon = MutableStateFlow(property.icon ?: "")

    private val showIconPicker = MutableStateFlow(false)
    private val showDeletionDialog = MutableStateFlow(false)

    override val state = combine(
        showIconPicker,
        showDeletionDialog,
        getMeasurementTypesUseCase(property),
        countMeasurementValuesOfProperty(property),
        MeasurementPropertyFormViewState::Loaded,
    )

    override fun handleIntent(intent: MeasurementPropertyFormIntent) {
        when (intent) {
            is MeasurementPropertyFormIntent.ShowIconPicker -> showIconPicker.value = true
            is MeasurementPropertyFormIntent.HideIconPicker -> showIconPicker.value = false
            is MeasurementPropertyFormIntent.UpdateProperty -> updateProperty()
            is MeasurementPropertyFormIntent.ShowDeletionDialog -> showDeletionDialog.value = true
            is MeasurementPropertyFormIntent.HideDeletionDialog -> showDeletionDialog.value = false
            is MeasurementPropertyFormIntent.DeleteProperty -> deleteProperty()
        }
    }

    private fun updateProperty() {
        updateProperty(property.copy(name = name.value, icon = icon.value))
        navigateBack()
    }

    private fun deleteProperty() {
        deleteProperty(property)
        showDeletionDialog.value = false
        navigateBack()
    }
}