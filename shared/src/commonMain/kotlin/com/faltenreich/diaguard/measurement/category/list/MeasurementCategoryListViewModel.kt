package com.faltenreich.diaguard.measurement.category.list

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.form.UpdateMeasurementCategoryUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.screen.MeasurementCategoryFormScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.map

class MeasurementCategoryListViewModel(
    getCategories: GetMeasurementCategoriesUseCase = inject(),
    private val updateCategory: UpdateMeasurementCategoryUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
) : ViewModel<MeasurementCategoryListViewState, MeasurementCategoryListIntent, Unit>() {

    override val state = getCategories().map(::MeasurementCategoryListViewState)

    private val categories: List<MeasurementCategory>?
        get() = stateInScope.value?.categories

    override fun handleIntent(intent: MeasurementCategoryListIntent) = with(intent) {
        when (this) {
            is MeasurementCategoryListIntent.DecrementSortIndex -> decrementSortIndex(category)
            is MeasurementCategoryListIntent.IncrementSortIndex -> incrementSortIndex(category)
            is MeasurementCategoryListIntent.Edit -> editCategory(category)
            is MeasurementCategoryListIntent.Create -> createCategory()
        }
    }

    private fun decrementSortIndex(category: MeasurementCategory) {
        val within = categories ?: return
        swapSortIndexes(first = category, second = within.last { it.sortIndex < category.sortIndex })
    }

    private fun incrementSortIndex(category: MeasurementCategory) {
        val within = categories ?: return
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
        navigateToScreen(MeasurementCategoryFormScreen())
    }
}