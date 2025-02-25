package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.navigation.modal.CloseModalUseCase
import com.faltenreich.diaguard.navigation.modal.OpenModalUseCase
import com.faltenreich.diaguard.navigation.screen.PopScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.view.AlertModal
import com.faltenreich.diaguard.shared.view.DeleteModal
import com.faltenreich.diaguard.shared.view.EmojiModal
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.delete_error_property
import diaguard.shared.generated.resources.delete_title
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MeasurementCategoryFormViewModel(
    categoryId: Long,
    getCategoryBdId: GetMeasurementCategoryBdIdUseCase = inject(),
    getProperties: GetMeasurementPropertiesUseCase = inject(),
    private val updateCategory: UpdateMeasurementCategoryUseCase = inject(),
    private val deleteCategory: DeleteMeasurementCategoryUseCase = inject(),
    private val popScreen: PopScreenUseCase = inject(),
    private val openModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
    private val localization: Localization = inject(),
) : ViewModel<MeasurementCategoryFormState, MeasurementCategoryFormIntent, Unit>() {

    val category: MeasurementCategory.Local = requireNotNull(getCategoryBdId(categoryId))

    var icon = MutableStateFlow(category.icon)
    var name = MutableStateFlow(category.name)
    var isActive = MutableStateFlow(category.isActive)

    private val properties = getProperties(category)

    override val state = properties.map(::MeasurementCategoryFormState)

    override suspend fun handleIntent(intent: MeasurementCategoryFormIntent) {
        when (intent) {
            is MeasurementCategoryFormIntent.OpenIconPicker -> openIconPicker()
            is MeasurementCategoryFormIntent.UpdateCategory -> updateCategory()
            is MeasurementCategoryFormIntent.DeleteCategory -> deleteCategory()
        }
    }

    private fun openIconPicker() = scope.launch {
        openModal(
            EmojiModal(
                onDismissRequest = { scope.launch { closeModal() } },
                onEmojiPicked = {
                    icon.value = it
                    scope.launch { closeModal() }
                },
            )
        )
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

    private fun deleteCategory() = scope.launch {
        if (category.isUserGenerated) {
            openModal(
                DeleteModal(
                    onDismissRequest = { scope.launch { closeModal() } },
                    onConfirmRequest = {
                        deleteCategory(category)
                        scope.launch {
                            closeModal()
                            popScreen()
                        }
                    }
                )
            )
        } else {
            openModal(
                AlertModal(
                    onDismissRequest = { scope.launch { closeModal() } },
                    title = localization.getString(Res.string.delete_title),
                    text = localization.getString(Res.string.delete_error_property),
                )
            )
        }
    }
}