package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.datetime.format.FormatDateTimeUseCase
import com.faltenreich.diaguard.datetime.picker.DateRangePickerModal
import com.faltenreich.diaguard.measurement.category.GetActiveMeasurementCategoriesUseCase
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.navigation.modal.CloseModalUseCase
import com.faltenreich.diaguard.navigation.modal.OpenModalUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class StatisticViewModel(
    getToday: GetTodayUseCase,
    getCategories: GetActiveMeasurementCategoriesUseCase,
    private val formatDateRange: FormatDateTimeUseCase,
    private val getAverage: GetAverageUseCase,
    private val openModal: OpenModalUseCase,
    private val closeModal: CloseModalUseCase,
) : ViewModel<StatisticState, StatisticIntent, Unit>() {

    private val dateRange = MutableStateFlow(getToday().let { it.minus(1, DateUnit.WEEK) .. it })
    private val category = MutableStateFlow<MeasurementCategory.Local?>(null)

    override val state = combine(
        dateRange,
        category,
        getCategories(),
    ) { dateRange, category, categories ->
        val category = category ?: categories.first()
        StatisticState(
            dateRange = formatDateRange(dateRange),
            category = category,
            categories = categories,
            average = getAverage(category, dateRange).first(), // TODO: Handle Flow
        )
    }

    override suspend fun handleIntent(intent: StatisticIntent) {
        when (intent) {
            is StatisticIntent.SetCategory -> category.value = intent.category
            is StatisticIntent.OpenDateRangePicker -> openDateRangePicker()
        }
    }

    private suspend fun openDateRangePicker() {
        openModal(
            DateRangePickerModal(
                dateRange = dateRange.value,
                onPick = {
                    dateRange.value =  it
                    scope.launch { closeModal() }
                },
            ),
        )
    }
}