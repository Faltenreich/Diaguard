package com.faltenreich.diaguard.statistic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.datetime.picker.DateRangePickerModal
import com.faltenreich.diaguard.measurement.category.GetActiveMeasurementCategoriesUseCase
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.navigation.modal.CloseModalUseCase
import com.faltenreich.diaguard.navigation.modal.OpenModalUseCase
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class StatisticViewModel(
    getToday: GetTodayUseCase,
    getCategories: GetActiveMeasurementCategoriesUseCase,
    private val getAverage: GetAverageUseCase,
    private val dateTimeFormatter: DateTimeFormatter,
    private val openModal: OpenModalUseCase,
    private val closeModal: CloseModalUseCase,
) : ViewModel<StatisticState, StatisticIntent, Unit>() {

    private val selectedCategory = MutableStateFlow<MeasurementCategory.Local?>(null)
    private val initialDateRange = getToday().let { today ->
        today.minus(1, DateUnit.WEEK) .. today
    }
    var dateRange by mutableStateOf(initialDateRange)
    val dateRangeLocalized: String
        get() = dateTimeFormatter.formatDateRange(dateRange)

    override val state = combine(
        getCategories(),
        selectedCategory,
    ) { categories, selectedCategory ->
        val category = selectedCategory ?: categories.first()
        StatisticState(
            categories = categories,
            selectedCategory = category,
            dateRange = dateRange,
            dateRangeLocalized = dateRangeLocalized,
            average = getAverage(category, dateRange),
        )
    }

    override suspend fun handleIntent(intent: StatisticIntent) {
        when (intent) {
            is StatisticIntent.SetCategory -> selectedCategory.value = intent.category
            is StatisticIntent.OpenDateRangePicker -> openDateRangePicker()
        }
    }

    private suspend fun openDateRangePicker() {
        openModal(
            DateRangePickerModal(
                dateRange = dateRange,
                onPick = {
                    dateRange =  it
                    scope.launch { closeModal() }
                },
            ),
        )
    }
}