package com.faltenreich.diaguard.statistic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.measurement.category.GetActiveMeasurementCategoriesUseCase
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.preference.DecimalPlaces
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class StatisticViewModel(
    getToday: GetTodayUseCase,
    getCategories: GetActiveMeasurementCategoriesUseCase,
    getPreference: GetPreferenceUseCase,
    private val getAverage: GetAverageUseCase,
    private val dateTimeFormatter: DateTimeFormatter,
) : ViewModel<StatisticViewState, StatisticIntent, Unit>() {

    private val selectedCategory = MutableStateFlow<MeasurementCategory.Local?>(null)
    private val initialDateRange = getToday().let { today ->
        today.minus(1, DateUnit.WEEK) .. today
    }
    var dateRange by mutableStateOf(initialDateRange)
    val dateRangeLocalized: String
        get() = dateTimeFormatter.formatDateRange(dateRange)

    override val state = combine(
        getCategories(),
        getPreference(DecimalPlaces),
        selectedCategory,
    ) { categories, decimalPlaces, selectedCategory ->
        val category = selectedCategory ?: categories.first()
        StatisticViewState(
            categories = categories,
            selectedCategory = category,
            dateRange = dateRange,
            dateRangeLocalized = dateRangeLocalized,
            average = getAverage(category, dateRange, decimalPlaces),
        )
    }

    override suspend fun handleIntent(intent: StatisticIntent) {
        when (intent) {
            is StatisticIntent.Select -> selectedCategory.value = intent.category
        }
    }
}