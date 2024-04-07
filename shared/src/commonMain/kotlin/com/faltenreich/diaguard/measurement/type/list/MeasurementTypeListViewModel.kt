package com.faltenreich.diaguard.measurement.type.list

import com.faltenreich.diaguard.measurement.property.form.CreateMeasurementTypeUseCase
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.form.UpdateMeasurementTypeUseCase
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.screen.MeasurementTypeFormScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MeasurementTypeListViewModel(
    private val updateMeasurementType: UpdateMeasurementTypeUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val createMeasurementType: CreateMeasurementTypeUseCase = inject(),
) : ViewModel<MeasurementTypeListViewState, MeasurementTypeListIntent, Unit>() {

    private val showFormDialog = MutableStateFlow(false)

    override val state = showFormDialog.map(::MeasurementTypeListViewState)

    override fun handleIntent(intent: MeasurementTypeListIntent) {
        when (intent) {
            is MeasurementTypeListIntent.DecrementSortIndex -> decrementSortIndex(intent.type, intent.inTypes)
            is MeasurementTypeListIntent.IncrementSortIndex -> incrementSortIndex(intent.type, intent.inTypes)
            is MeasurementTypeListIntent.EditType -> navigateToScreen(MeasurementTypeFormScreen(intent.type))
            is MeasurementTypeListIntent.ShowFormDialog -> showFormDialog.value  = true
            is MeasurementTypeListIntent.HideFormDialog -> showFormDialog.value = false
            is MeasurementTypeListIntent.CreateType -> createMeasurementType(
                typeKey = null,
                typeName = intent.typeName,
                // TODO: Make user-customizable
                typeRange = MeasurementValueRange(
                    minimum = 0.0,
                    low = null,
                    target = null,
                    high = null,
                    maximum = Double.MAX_VALUE,
                    isHighlighted = false,
                ),
                typeSortIndex = intent.types.maxOfOrNull(MeasurementType::sortIndex)?.plus(1) ?: 0,
                propertyId = intent.propertyId,
                unitKey = null,
                unitName = intent.unitName,
            )
        }
    }

    private fun decrementSortIndex(
        type: MeasurementType,
        inTypes: List<MeasurementType>,
    ) {
        swapSortIndexes(first = type, second = inTypes.last { it.sortIndex < type.sortIndex })
    }

    private fun incrementSortIndex(
        type: MeasurementType,
        inTypes: List<MeasurementType>,
    ) {
        swapSortIndexes(first = type, second = inTypes.first { it.sortIndex > type.sortIndex })
    }

    private fun swapSortIndexes(
        first: MeasurementType,
        second: MeasurementType,
    ) {
        updateMeasurementType(first.copy(sortIndex = second.sortIndex))
        updateMeasurementType(second.copy(sortIndex = first.sortIndex))
    }
}