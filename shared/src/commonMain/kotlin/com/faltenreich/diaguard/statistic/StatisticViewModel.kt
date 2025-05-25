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
import com.faltenreich.diaguard.statistic.category.StatisticCategoryState
import com.faltenreich.diaguard.statistic.distribution.GetStatisticDistributionUseCase
import com.faltenreich.diaguard.statistic.property.StatisticPropertyState
import com.faltenreich.diaguard.statistic.trend.GetStatisticTrendUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
    private val categories = MutableStateFlow(emptyList<MeasurementCategory.Local>())
    private val category = MutableStateFlow<MeasurementCategory.Local?>(null)
    private val properties = MutableStateFlow(emptyList<MeasurementProperty.Local>())
    private val property = MutableStateFlow<MeasurementProperty.Local?>(null)

    override val state = combine(
        dateRange,
        category,
        property,
    ) { dateRange, category, property ->
        Triple(dateRange, category, property)
    }.flatMapLatest { (dateRange, category, property) ->
        if (category != null && property != null) {
            combine(
                categories,
                properties,
                getAverage(category, dateRange),
                getTrend(category, dateRange),
                getDistribution(property, dateRange),
            ) { categories, properties, average, trend, distribution ->
                StatisticState(
                    dateRange = dateRange,
                    dateRangeLocalized = formatDateRange(dateRange),
                    category = StatisticCategoryState(
                        categories = categories,
                        selection = category,
                    ),
                    property = StatisticPropertyState(
                        properties = properties,
                        selection = property,
                    ),
                    average = average,
                    trend = trend,
                    distribution = distribution,
                )
            }
        } else {
            flowOf(
                StatisticState(
                    dateRange = dateRange,
                    dateRangeLocalized = formatDateRange(dateRange),
                )
            )
        }
    }

    init {
        scope.launch {
            getCategories().collectLatest { categories ->
                this@StatisticViewModel.categories.update { categories }
                val category = categories.firstOrNull() ?: return@collectLatest
                this@StatisticViewModel.category.update { category }

            }
        }
        scope.launch {
            category.collectLatest { category ->
                when (category) {
                    null -> {
                        properties.update { emptyList() }
                        property.update { null }
                    }
                    else -> {
                        getProperties(category).collectLatest { properties ->
                            this@StatisticViewModel.properties.update { properties }
                            property.update { properties.firstOrNull() }
                        }
                    }
                }
            }
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