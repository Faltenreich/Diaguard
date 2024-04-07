package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.modal.DeleteModal
import com.faltenreich.diaguard.navigation.modal.EmojiModal
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MeasurementPropertyFormViewModel(
    val property: MeasurementProperty,
    getMeasurementTypesUseCase: GetMeasurementTypesUseCase = inject(),
    private val updateProperty: UpdateMeasurementPropertyUseCase = inject(),
    private val deleteProperty: DeleteMeasurementPropertyUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
    private val openModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
) : ViewModel<MeasurementPropertyFormViewState, MeasurementPropertyFormIntent, Unit>() {

    var name = MutableStateFlow(property.name)
    var icon = MutableStateFlow(property.icon ?: "")

    override val state = getMeasurementTypesUseCase(property).map(::MeasurementPropertyFormViewState)

    override fun handleIntent(intent: MeasurementPropertyFormIntent) {
        when (intent) {
            is MeasurementPropertyFormIntent.OpenIconPicker -> openIconPicker()
            is MeasurementPropertyFormIntent.UpdateProperty -> updateProperty()
            is MeasurementPropertyFormIntent.DeleteProperty -> deleteProperty()
        }
    }

    private fun openIconPicker() {
        openModal(
            EmojiModal(
                onDismissRequest = closeModal::invoke,
                onEmojiPicked = { icon.value = it },
            )
        )
    }

    private fun updateProperty() {
        updateProperty(property.copy(name = name.value, icon = icon.value))
        navigateBack()
    }

    private fun deleteProperty() {
        openModal(
            DeleteModal(
                onDismissRequest = closeModal::invoke,
                onConfirmRequest = {
                    deleteProperty(property)
                    closeModal()
                    navigateBack()
                }
            )
        )
    }
}