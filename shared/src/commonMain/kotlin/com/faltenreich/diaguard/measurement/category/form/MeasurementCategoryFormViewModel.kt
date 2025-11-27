package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.navigation.NavigationTarget
import com.faltenreich.diaguard.data.preference.color.ColorSchemePreference
import com.faltenreich.diaguard.injection.inject
import com.faltenreich.diaguard.measurement.category.usecase.DeleteMeasurementCategoryUseCase
import com.faltenreich.diaguard.measurement.category.usecase.GetMeasurementCategoryByIdUseCase
import com.faltenreich.diaguard.measurement.category.usecase.StoreMeasurementCategoryUseCase
import com.faltenreich.diaguard.measurement.property.usecase.GetMeasurementPropertiesUseCase
import com.faltenreich.diaguard.measurement.property.usecase.StoreMeasurementPropertyUseCase
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.NavigateToUseCase
import com.faltenreich.diaguard.navigation.ShowSnackbarUseCase
import com.faltenreich.diaguard.preference.GetPreferenceUseCase
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.error_unknown
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

class MeasurementCategoryFormViewModel(
    categoryId: Long,
    getCategoryBdId: GetMeasurementCategoryByIdUseCase = inject(),
    getProperties: GetMeasurementPropertiesUseCase = inject(),
    getPreference: GetPreferenceUseCase = inject(),
    private val storeCategory: StoreMeasurementCategoryUseCase = inject(),
    private val storeProperty: StoreMeasurementPropertyUseCase = inject(),
    private val deleteCategory: DeleteMeasurementCategoryUseCase = inject(),
    private val navigateTo: NavigateToUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
    private val showSnackbar: ShowSnackbarUseCase = inject(),
) : ViewModel<MeasurementCategoryFormState, MeasurementCategoryFormIntent, Unit>() {

    private val category: MeasurementCategory.Local = checkNotNull(getCategoryBdId(categoryId))
    private val name = MutableStateFlow(category.name)
    private val icon = MutableStateFlow(category.icon)
    private val isActive = MutableStateFlow(category.isActive)

    private val properties = getProperties(category)
    private val deleteDialog = MutableStateFlow<MeasurementCategoryFormState.DeleteDialog?>(null)
    private val alertDialog = MutableStateFlow<MeasurementCategoryFormState.AlertDialog?>(null)

    override val state = combine(
        name,
        icon,
        isActive,
        properties,
        getPreference(ColorSchemePreference),
        deleteDialog,
        alertDialog,
        ::MeasurementCategoryFormState,
    )

    override suspend fun handleIntent(intent: MeasurementCategoryFormIntent) {
        when (intent) {
            is MeasurementCategoryFormIntent.SetName ->
                name.update { intent.name }
            is MeasurementCategoryFormIntent.SetIcon ->
                icon.update { intent.icon }
            is MeasurementCategoryFormIntent.SetIsActive ->
                isActive.update { intent.isActive }
            is MeasurementCategoryFormIntent.DecrementSortIndex ->
                decrementSortIndex(intent.property, intent.inProperties)
            is MeasurementCategoryFormIntent.IncrementSortIndex ->
                incrementSortIndex(intent.property, intent.inProperties)
            is MeasurementCategoryFormIntent.EditProperty ->
                editProperty(intent.property)
            is MeasurementCategoryFormIntent.AddProperty ->
                navigateTo(NavigationTarget.MeasurementPropertyForm(categoryId = category.id))
            is MeasurementCategoryFormIntent.Store ->
                updateCategory()
            is MeasurementCategoryFormIntent.Delete ->
                deleteCategory(intent.needsConfirmation)
            is MeasurementCategoryFormIntent.OpenDeleteDialog ->
                deleteDialog.update { MeasurementCategoryFormState.DeleteDialog }
            is MeasurementCategoryFormIntent.CloseDeleteDialog ->
                deleteDialog.update { null }
            is MeasurementCategoryFormIntent.OpenAlertDialog ->
                alertDialog.update { MeasurementCategoryFormState.AlertDialog }
            is MeasurementCategoryFormIntent.CloseAlertDialog ->
                alertDialog.update { null }
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
        storeProperty(first.copy(sortIndex = second.sortIndex))
        storeProperty(second.copy(sortIndex = first.sortIndex))
    }

    private suspend fun editProperty(property: MeasurementProperty.Local) {
        navigateTo(
            NavigationTarget.MeasurementPropertyForm(
                categoryId = property.category.id,
                propertyId = property.id,
            ),
        )
    }

    // TODO: Validate
    private suspend fun updateCategory() {
        val category = category.copy(
            name = name.value,
            icon = icon.value?.takeIf(String::isNotBlank),
            isActive = isActive.value,
        )
        storeCategory(category)
        navigateBack()
    }

    private suspend fun deleteCategory(needsConfirmation: Boolean) {
        if (category.isUserGenerated) {
            if (needsConfirmation) {
                deleteDialog.update { MeasurementCategoryFormState.DeleteDialog }
            } else {
                deleteCategory(category)
                navigateBack()
            }
        } else {
            alertDialog.update { MeasurementCategoryFormState.AlertDialog }
        }
    }
}