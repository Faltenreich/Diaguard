package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.navigation.screen.PopScreenUseCase
import com.faltenreich.diaguard.preference.color.ColorSchemePreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

class MeasurementCategoryFormViewModel(
    categoryId: Long,
    getCategoryBdId: GetMeasurementCategoryBdIdUseCase = inject(),
    getProperties: GetMeasurementPropertiesUseCase = inject(),
    getPreference: GetPreferenceUseCase = inject(),
    private val updateCategory: UpdateMeasurementCategoryUseCase = inject(),
    private val deleteCategory: DeleteMeasurementCategoryUseCase = inject(),
    private val popScreen: PopScreenUseCase = inject(),
) : ViewModel<MeasurementCategoryFormState, MeasurementCategoryFormIntent, Unit>() {

    val category: MeasurementCategory.Local = checkNotNull(getCategoryBdId(categoryId))

    var icon = MutableStateFlow(category.icon)
    var name = MutableStateFlow(category.name)
    var isActive = MutableStateFlow(category.isActive)

    private val properties = getProperties(category)
    private val deleteDialog = MutableStateFlow<MeasurementCategoryFormState.DeleteDialog?>(null)
    private val alertDialog = MutableStateFlow<MeasurementCategoryFormState.AlertDialog?>(null)

    override val state = combine(
        properties,
        getPreference(ColorSchemePreference),
        deleteDialog,
        alertDialog,
        ::MeasurementCategoryFormState,
    )

    override suspend fun handleIntent(intent: MeasurementCategoryFormIntent) = with(intent) {
        when (this) {
            is MeasurementCategoryFormIntent.Store ->
                updateCategory()
            is MeasurementCategoryFormIntent.Delete ->
                deleteCategory(needsConfirmation)
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

    // TODO: Validate
    private suspend fun updateCategory() {
        val category = category.copy(
            name = name.value,
            icon = icon.value?.takeIf(String::isNotBlank),
            isActive = isActive.value,
        )
        updateCategory(category)
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