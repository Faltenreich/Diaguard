package com.faltenreich.diaguard.measurement.category.list

import com.faltenreich.diaguard.measurement.category.GetAllMeasurementCategoriesUseCase
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.form.UpdateMeasurementCategoryUseCase
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.modal.MeasurementCategoryFormModal
import com.faltenreich.diaguard.navigation.screen.MeasurementCategoryFormScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.map

class MeasurementCategoryListViewModel(
    getCategories: GetAllMeasurementCategoriesUseCase,
    private val updateCategory: UpdateMeasurementCategoryUseCase,
    private val createCategory: CreateMeasurementCategoryUseCase,
    private val navigateToScreen: NavigateToScreenUseCase,
    private val openModal: OpenModalUseCase,
    private val closeModal: CloseModalUseCase,
) : ViewModel<MeasurementCategoryListViewState, MeasurementCategoryListIntent, Unit>() {

    override val state = getCategories().map(::MeasurementCategoryListViewState)

    override fun handleIntent(intent: MeasurementCategoryListIntent) = with(intent) {
        when (this) {
            is MeasurementCategoryListIntent.DecrementSortIndex -> decrementSortIndex(category)
            is MeasurementCategoryListIntent.IncrementSortIndex -> incrementSortIndex(category)
            is MeasurementCategoryListIntent.Edit -> editCategory(category)
            is MeasurementCategoryListIntent.Create -> createCategory()
        }
    }

    private fun decrementSortIndex(category: MeasurementCategory) {
        val within = stateInScope.value?.categories ?: return
        swapSortIndexes(first = category, second = within.last { it.sortIndex < category.sortIndex })
    }

    private fun incrementSortIndex(category: MeasurementCategory) {
        val within = stateInScope.value?.categories ?: return
        swapSortIndexes(first = category, second = within.first { it.sortIndex > category.sortIndex })
    }

    private fun swapSortIndexes(
        first: MeasurementCategory,
        second: MeasurementCategory,
    ) {
        updateCategory(first.copy(sortIndex = second.sortIndex))
        updateCategory(second.copy(sortIndex = first.sortIndex))
    }

    private fun editCategory(category: MeasurementCategory) {
        navigateToScreen(MeasurementCategoryFormScreen(category))
    }

    private fun createCategory() {
        val within = stateInScope.value?.categories ?: return
        openModal(
            MeasurementCategoryFormModal(
                onDismissRequest = closeModal::invoke,
                onConfirmRequest = { name ->
                    val category = createCategory(
                        name = name,
                        icon = null,
                        sortIndex = within.maxOf(MeasurementCategory::sortIndex) + 1,
                    )
                    closeModal()
                    editCategory(category)
                }
            )
        )
    }
}