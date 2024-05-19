package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.modal.AlertModal
import com.faltenreich.diaguard.navigation.modal.DeleteModal
import com.faltenreich.diaguard.navigation.modal.EmojiModal
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.delete_error_property
import diaguard.shared.generated.resources.delete_title
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MeasurementCategoryFormViewModel(
    val category: MeasurementCategory.Local,
    private val localization: Localization = inject(),
    getProperties: GetMeasurementPropertiesUseCase = inject(),
    private val updateCategory: UpdateMeasurementCategoryUseCase = inject(),
    private val deleteCategory: DeleteMeasurementCategoryUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
    private val openModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
) : ViewModel<MeasurementCategoryFormViewState, MeasurementCategoryFormIntent, Unit>() {

    var icon = MutableStateFlow(category.icon)
    var name = MutableStateFlow(category.name)
    var isActive = MutableStateFlow(category.isActive)

    private val properties = getProperties(category)

    override val state = properties.map(::MeasurementCategoryFormViewState)

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
                onEmojiPicked = {
                    icon.value = it
                    closeModal()
                },
            )
        )
    }

    // TODO: Validate
    private fun updateCategory() {
        val category = category.copy(
            name = name.value,
            icon = icon.value?.takeIf(String::isNotBlank),
            isActive = isActive.value,
        )
        updateCategory(category)
        navigateBack()
    }

    private fun deleteCategory() {
        if (category.isUserGenerated) {
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
        } else {
            openModal(
                AlertModal(
                    onDismissRequest = closeModal::invoke,
                    title = localization.getString(Res.string.delete_title),
                    text = localization.getString(Res.string.delete_error_property),
                )
            )
        }
    }
}