package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.datetime.factory.DateTimeConstants
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.architecture.combine
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class MeasurementPropertyFormViewModel(
    property: MeasurementProperty,
    getMeasurementTypesUseCase: GetMeasurementTypesUseCase = inject(),
    countMeasurementValuesOfProperty: CountMeasurementValuesOfPropertyUseCase = inject(),
    updateMeasurementProperty: UpdateMeasurementPropertyUseCase = inject(),
    private val createMeasurementType: CreateMeasurementTypeUseCase = inject(),
    private val deleteMeasurementProperty: DeleteMeasurementPropertyUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
) : ViewModel<MeasurementPropertyFormViewState, MeasurementPropertyFormIntent>() {

    var name = MutableStateFlow(property.name)
    var icon = MutableStateFlow(property.icon ?: "")

    private val showIconPicker = MutableStateFlow(false)
    private val showFormDialog = MutableStateFlow(false)
    private val showDeletionDialog = MutableStateFlow(false)

    override val state = combine(
        flowOf(property),
        showIconPicker,
        showFormDialog,
        showDeletionDialog,
        getMeasurementTypesUseCase(property),
        countMeasurementValuesOfProperty(property),
        MeasurementPropertyFormViewState::Loaded,
    )

    init {
        // FIXME: Setting both at the same time cancels the first collector
        scope.launch {
            name.debounce(DateTimeConstants.INPUT_DEBOUNCE)
                .collectLatest { name -> updateMeasurementProperty(property.copy(name = name)) }
        }
        scope.launch {
            icon.debounce(DateTimeConstants.INPUT_DEBOUNCE)
                .collectLatest { icon -> updateMeasurementProperty(property.copy(icon = icon)) }
        }
    }

    override fun onIntent(intent: MeasurementPropertyFormIntent) {
        when (intent) {
            is MeasurementPropertyFormIntent.ShowIconPicker -> showIconPicker.value = true
            is MeasurementPropertyFormIntent.HideIconPicker -> showIconPicker.value = false
            is MeasurementPropertyFormIntent.ShowFormDialog -> showFormDialog.value  = true
            is MeasurementPropertyFormIntent.HideFormDialog -> showFormDialog.value = false
            is MeasurementPropertyFormIntent.CreateType -> createMeasurementType(
                typeKey = null,
                typeName = intent.typeName,
                // TODO: Make user-customizable
                typeMinimumValue = 0.0,
                typeLowValue = null,
                typeTargetValue = null,
                typeHighValue = null,
                typeMaximumValue = Double.MAX_VALUE,
                typeSortIndex = intent.types.maxOfOrNull(MeasurementType::sortIndex)?.plus(1) ?: 0,
                propertyId = intent.propertyId,
                unitKey = null,
                unitName = intent.unitName,
            )
            is MeasurementPropertyFormIntent.ShowDeletionDialog -> showDeletionDialog.value = true
            is MeasurementPropertyFormIntent.HideDeletionDialog -> showDeletionDialog.value = false
            is MeasurementPropertyFormIntent.DeleteProperty -> {
                deleteMeasurementProperty(intent.property)
                showDeletionDialog.value = false
                navigateBack()
            }
        }
    }
}