package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.modal.DeleteModal
import com.faltenreich.diaguard.navigation.modal.EmojiModal
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MeasurementCategoryFormViewModel(
    val category: MeasurementCategory,
    getMeasurementPropertiesUseCase: GetMeasurementPropertiesUseCase = inject(),
    private val updateCategory: UpdateMeasurementCategoryUseCase = inject(),
    private val deleteCategory: DeleteMeasurementCategoryUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
    private val openModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
) : ViewModel<MeasurementCategoryFormViewState, MeasurementCategoryFormIntent, Unit>() {

    var name = MutableStateFlow(category.name)
    var icon = MutableStateFlow(category.icon ?: "")

    override val state = getMeasurementPropertiesUseCase(category).map(::MeasurementCategoryFormViewState)

    override fun handleIntent(intent: MeasurementCategoryFormIntent) {
        when (intent) {
            is MeasurementCategoryFormIntent.OpenIconPicker -> openIconPicker()
            is MeasurementCategoryFormIntent.UpdateCategory -> updateCategory()
            is MeasurementCategoryFormIntent.DeleteCategory -> deleteCategory()
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

    private fun updateCategory() {
        updateCategory(category.copy(name = name.value, icon = icon.value))
        navigateBack()
    }

    private fun deleteCategory() {
        openModal(
            DeleteModal(
                onDismissRequest = closeModal::invoke,
                onConfirmRequest = {
                    deleteCategory(category)
                    closeModal()
                    navigateBack()
                }
            )
        )
    }
}