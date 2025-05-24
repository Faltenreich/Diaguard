package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.datetime.format.FormatDateTimeUseCase
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.usecase.GetActiveMeasurementCategoriesUseCase
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.usecase.GetMeasurementPropertiesUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.statistic.average.GetStatisticAverageUseCase
import com.faltenreich.diaguard.statistic.distribution.GetStatisticDistributionUseCase
import com.faltenreich.diaguard.statistic.trend.GetStatisticTrendUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest

class StatisticViewModel(
    getToday: GetTodayUseCase,
    getCategories: GetActiveMeasurementCategoriesUseCase,
    getProperties: GetMeasurementPropertiesUseCase,
    private val formatDateRange: FormatDateTimeUseCase,
    private val getAverage: GetStatisticAverageUseCase,
    private val getTrend: GetStatisticTrendUseCase,
    private val getDistribution: GetStatisticDistributionUseCase,
) : ViewModel<StatisticState, StatisticIntent, Unit>() {

    private val dateRange = MutableStateFlow(getToday().let { it.minus(1, DateUnit.WEEK) .. it })
    private val category = MutableStateFlow<MeasurementCategory.Local?>(null)
    private val property = MutableStateFlow<MeasurementProperty.Local?>(null)

    override val state = combine(
        dateRange,
        getCategories(),
        category,
    ) { dateRange, categories, category ->
        Triple(dateRange, categories, category ?: categories.first())
    }.flatMapLatest { (dateRange, categories, category) ->
        combine(
            getProperties(category),
            getAverage(category, dateRange),
            getTrend(category, dateRange),
            getDistribution(category, dateRange),
        ) { properties, average, trend, distribution ->
            StatisticState(
                dateRange = dateRange,
                dateRangeLocalized = formatDateRange(dateRange),
                categories = categories,
                category = category,
                properties = properties,
                property = TODO(),
                average = average,
                trend = trend,
                distribution = distribution,
            )
        }
    }

    override suspend fun handleIntent(intent: StatisticIntent) {
        when (intent) {
            is StatisticIntent.SetDateRange -> dateRange.value = intent.dateRange
            is StatisticIntent.SetCategory -> category.value = intent.category
            is StatisticIntent.SetProperty -> property.value = intent.property
        }
    }
}