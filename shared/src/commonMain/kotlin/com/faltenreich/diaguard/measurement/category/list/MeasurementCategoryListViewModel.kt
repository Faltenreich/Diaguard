package com.faltenreich.diaguard.measurement.category.list

import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.form.MeasurementCategoryFormScreen
import com.faltenreich.diaguard.measurement.category.usecase.GetMeasurementCategoriesUseCase
import com.faltenreich.diaguard.measurement.category.usecase.StoreMeasurementCategoryUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update

class MeasurementCategoryListViewModel(
    getCategories: GetMeasurementCategoriesUseCase,
    private val storeCategory: StoreMeasurementCategoryUseCase,
    private val pushScreen: PushScreenUseCase,
) : ViewModel<MeasurementCategoryListState, MeasurementCategoryListIntent, Unit>() {

    private val formDialog = MutableStateFlow<MeasurementCategoryListState.FormDialog?>(null)

    override val state = combine(
        getCategories(),
        formDialog,
        ::MeasurementCategoryListState,
    )

    override suspend fun handleIntent(intent: MeasurementCategoryListIntent) = with(intent) {
        when (this) {
            is MeasurementCategoryListIntent.DecrementSortIndex ->
                decrementSortIndex(category)
            is MeasurementCategoryListIntent.IncrementSortIndex ->
                incrementSortIndex(category)
            is MeasurementCategoryListIntent.Edit ->
                editCategory(category)
            is MeasurementCategoryListIntent.OpenFormDialog ->
                formDialog.update { MeasurementCategoryListState.FormDialog }
            is MeasurementCategoryListIntent.CloseFormDialog ->
                formDialog.update { null }
            is MeasurementCategoryListIntent.Create ->
                createCategory(this)
        }
    }

    private suspend fun decrementSortIndex(category: MeasurementCategory.Local) {
        val within = state.firstOrNull()?.categories ?: return
        swapSortIndexes(first = category, second = within.last { it.sortIndex < category.sortIndex })
    }

    private suspend fun incrementSortIndex(category: MeasurementCategory.Local) {
        val within = state.firstOrNull()?.categories ?: return
        swapSortIndexes(first = category, second = within.first { it.sortIndex > category.sortIndex })
    }

    private fun swapSortIndexes(
        first: MeasurementCategory.Local,
        second: MeasurementCategory.Local,
    ) {
        storeCategory(first.copy(sortIndex = second.sortIndex))
        storeCategory(second.copy(sortIndex = first.sortIndex))
    }

    private suspend fun editCategory(category: MeasurementCategory.Local) {
        pushScreen(MeasurementCategoryFormScreen(category))
    }

    private suspend fun createCategory(intent: MeasurementCategoryListIntent.Create) {
        // TODO: Harden
        val within = checkNotNull(state.firstOrNull()?.categories)
        val category = storeCategory(
            MeasurementCategory.User(
                name = intent.name,
                icon = null,
                sortIndex = within.maxOf(MeasurementCategory::sortIndex) + 1,
                isActive = true,
            )
        )
        editCategory(category)
    }
}