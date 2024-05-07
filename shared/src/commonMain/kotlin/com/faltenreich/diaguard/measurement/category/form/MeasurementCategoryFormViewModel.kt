package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.list.CreateMeasurementCategoryUseCase
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
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class MeasurementCategoryFormViewModel(
    val category: MeasurementCategory?,
    private val localization: Localization = inject(),
    getProperties: GetMeasurementPropertiesUseCase = inject(),
    private val createCategory: CreateMeasurementCategoryUseCase = inject(),
    private val updateCategory: UpdateMeasurementCategoryUseCase = inject(),
    private val deleteCategory: DeleteMeasurementCategoryUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
    private val openModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
) : ViewModel<MeasurementCategoryFormViewState, MeasurementCategoryFormIntent, Unit>() {

    var name = MutableStateFlow(category?.name ?: "")
    var icon = MutableStateFlow(category?.icon)

    private val properties = category?.let(getProperties::invoke) ?: emptyFlow()

    override val state = properties.map(::MeasurementCategoryFormViewState)

    override fun handleIntent(intent: MeasurementCategoryFormIntent) {
        when (intent) {
            is MeasurementCategoryFormIntent.OpenIconPicker -> openIconPicker()
            is MeasurementCategoryFormIntent.Confirm -> confirm()
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

    // TODO: Validate
    private fun confirm() {
        val category = category
        val name = name.value
        val icon = icon.value?.takeIf(String::isNotBlank)
        if (category != null) {
            updateCategory(category.copy(name = name, icon = icon))
        } else {
            createCategory(name = name, icon = icon)
        }
        navigateBack()
    }

    private fun deleteCategory() {
        val category = category ?: return
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