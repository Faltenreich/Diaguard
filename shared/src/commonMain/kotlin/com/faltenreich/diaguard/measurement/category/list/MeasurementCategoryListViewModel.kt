package com.faltenreich.diaguard.measurement.category.list

import com.faltenreich.diaguard.measurement.category.GetAllMeasurementCategoriesUseCase
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.form.MeasurementCategoryFormModal
import com.faltenreich.diaguard.measurement.category.form.MeasurementCategoryFormScreen
import com.faltenreich.diaguard.measurement.category.form.UpdateMeasurementCategoryUseCase
import com.faltenreich.diaguard.navigation.modal.CloseModalUseCase
import com.faltenreich.diaguard.navigation.modal.OpenModalUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MeasurementCategoryListViewModel(
    getCategories: GetAllMeasurementCategoriesUseCase,
    private val updateCategory: UpdateMeasurementCategoryUseCase,
    private val createCategory: CreateMeasurementCategoryUseCase,
    private val pushScreen: PushScreenUseCase,
    private val openModal: OpenModalUseCase,
    private val closeModal: CloseModalUseCase,
) : ViewModel<MeasurementCategoryListState, MeasurementCategoryListIntent, Unit>() {

    override val state = getCategories().map(::MeasurementCategoryListState)

    override suspend fun handleIntent(intent: MeasurementCategoryListIntent) = with(intent) {
        when (this) {
            is MeasurementCategoryListIntent.DecrementSortIndex -> decrementSortIndex(category)
            is MeasurementCategoryListIntent.IncrementSortIndex -> incrementSortIndex(category)
            is MeasurementCategoryListIntent.Edit -> editCategory(category)
            is MeasurementCategoryListIntent.Create -> createCategory()
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
        updateCategory(first.copy(sortIndex = second.sortIndex))
        updateCategory(second.copy(sortIndex = first.sortIndex))
    }

    private suspend fun editCategory(category: MeasurementCategory.Local) {
        pushScreen(MeasurementCategoryFormScreen(category))
    }

    private suspend fun createCategory() {
        val within = state.firstOrNull()?.categories ?: return
        openModal(
            MeasurementCategoryFormModal(
                onDismissRequest = { scope.launch { closeModal() } },
                onConfirmRequest = { name ->
                    val category = createCategory(
                        MeasurementCategory.User(
                            name = name,
                            icon = null,
                            sortIndex = within.maxOf(MeasurementCategory::sortIndex) + 1,
                            isActive = true,
                        )
                    )
                    scope.launch {
                        closeModal()
                        editCategory(category)
                    }
                }
            )
        )
    }
}