package com.faltenreich.diaguard.measurement.category.list

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.form.UpdateMeasurementCategoryUseCase
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.modal.MeasurementCategoryFormModal
import com.faltenreich.diaguard.navigation.screen.MeasurementCategoryFormScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.map

class MeasurementCategoryListViewModel(
    getMeasurementCategories: GetMeasurementCategoriesUseCase = inject(),
    private val updateMeasurementCategory: UpdateMeasurementCategoryUseCase = inject(),
    private val createMeasurementCategory: CreateMeasurementCategoryUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val openModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
) : ViewModel<MeasurementCategoryListViewState, MeasurementCategoryListIntent, Unit>() {

    override val state = getMeasurementCategories().map(::MeasurementCategoryListViewState)

    override fun handleIntent(intent: MeasurementCategoryListIntent) = with(intent) {
        when (this) {
            is MeasurementCategoryListIntent.ChangeIsActive -> changeIsActive(category)
            is MeasurementCategoryListIntent.DecrementSortIndex -> decrementSortIndex(category)
            is MeasurementCategoryListIntent.IncrementSortIndex -> incrementSortIndex(category)
            is MeasurementCategoryListIntent.Edit -> editCategory(category)
            is MeasurementCategoryListIntent.Create -> createCategory()
        }
    }

    private fun changeIsActive(category: MeasurementCategory) {
        updateMeasurementCategory(category.copy(isActive = !category.isActive))
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
        updateMeasurementCategory(first.copy(sortIndex = second.sortIndex))
        updateMeasurementCategory(second.copy(sortIndex = first.sortIndex))
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
                    val category = createMeasurementCategory(
                        name = name,
                        icon = null,
                        sortIndex = within.maxOf(MeasurementCategory::sortIndex) + 1,
                    )
                    closeModal()
                    navigateToScreen(MeasurementCategoryFormScreen(category))
                }
            )
        )
    }
}