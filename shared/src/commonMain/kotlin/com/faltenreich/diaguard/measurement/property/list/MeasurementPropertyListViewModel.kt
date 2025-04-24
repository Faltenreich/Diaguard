package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormScreen
import com.faltenreich.diaguard.measurement.property.form.UpdateMeasurementPropertyUseCase
import com.faltenreich.diaguard.navigation.bar.snackbar.ShowSnackbarUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.error_unknown
import kotlinx.coroutines.flow.emptyFlow

class MeasurementPropertyListViewModel(
    private val updateProperty: UpdateMeasurementPropertyUseCase,
    private val pushScreen: PushScreenUseCase,
    private val showSnackbar: ShowSnackbarUseCase,
) : ViewModel<Unit, MeasurementPropertyListIntent, Unit>() {

    override val state = emptyFlow<Unit>()

    override suspend fun handleIntent(intent: MeasurementPropertyListIntent) = with(intent) {
        when (this) {
            is MeasurementPropertyListIntent.DecrementSortIndex -> decrementSortIndex(property, inProperties)
            is MeasurementPropertyListIntent.IncrementSortIndex -> incrementSortIndex(property, inProperties)
            is MeasurementPropertyListIntent.EditProperty -> editProperty(property)
            is MeasurementPropertyListIntent.AddProperty -> pushScreen(MeasurementPropertyFormScreen())
        }
    }

    private suspend fun decrementSortIndex(
        property: MeasurementProperty.Local,
        inProperties: List<MeasurementProperty.Local>,
    ) {
        val previous = inProperties.lastOrNull { it.sortIndex < property.sortIndex } ?: run {
            showSnackbar(Res.string.error_unknown)
            return
        }
        swapSortIndexes(first = property, second = previous)
    }

    private suspend fun incrementSortIndex(
        property: MeasurementProperty.Local,
        inProperties: List<MeasurementProperty.Local>,
    ) {
        val next = inProperties.firstOrNull { it.sortIndex > property.sortIndex } ?: run {
            showSnackbar(Res.string.error_unknown)
            return
        }
        swapSortIndexes(first = property, second = next)
    }

    private fun swapSortIndexes(
        first: MeasurementProperty.Local,
        second: MeasurementProperty.Local,
    ) {
        updateProperty(first.copy(sortIndex = second.sortIndex))
        updateProperty(second.copy(sortIndex = first.sortIndex))
    }

    private suspend fun editProperty(property: MeasurementProperty.Local) {
        pushScreen(MeasurementPropertyFormScreen(property))
    }
}