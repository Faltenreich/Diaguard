package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.usecase.DeleteMeasurementCategoryUseCase
import com.faltenreich.diaguard.measurement.category.usecase.GetMeasurementCategoryByIdUseCase
import com.faltenreich.diaguard.measurement.category.usecase.StoreMeasurementCategoryUseCase
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormScreen
import com.faltenreich.diaguard.measurement.property.usecase.GetMeasurementPropertiesUseCase
import com.faltenreich.diaguard.measurement.property.usecase.StoreMeasurementPropertyUseCase
import com.faltenreich.diaguard.navigation.bar.snackbar.ShowSnackbarUseCase
import com.faltenreich.diaguard.navigation.screen.PopScreenUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.preference.color.ColorSchemePreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.localization.di.inject
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.error_unknown
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
    private val pushScreen: PushScreenUseCase = inject(),
    private val popScreen: PopScreenUseCase = inject(),
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
                pushScreen(MeasurementPropertyFormScreen(category))
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
        pushScreen(MeasurementPropertyFormScreen(property))
    }

    // TODO: Validate
    private suspend fun updateCategory() {
        val category = category.copy(
            name = name.value,
            icon = icon.value?.takeIf(String::isNotBlank),
            isActive = isActive.value,
        )
        storeCategory(category)
        popScreen()
    }

    private suspend fun deleteCategory(needsConfirmation: Boolean) {
        if (category.isUserGenerated) {
            if (needsConfirmation) {
                deleteDialog.update { MeasurementCategoryFormState.DeleteDialog }
            } else {
                deleteCategory(category)
                popScreen()
            }
        } else {
            alertDialog.update { MeasurementCategoryFormState.AlertDialog }
        }
    }
}