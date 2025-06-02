package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.usecase.GetActiveMeasurementCategoriesUseCase
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.usecase.GetMeasurementPropertiesUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.localization.format
import com.faltenreich.diaguard.statistic.average.GetStatisticAverageUseCase
import com.faltenreich.diaguard.statistic.category.StatisticCategoryState
import com.faltenreich.diaguard.statistic.daterange.StatisticDateRangeState
import com.faltenreich.diaguard.statistic.daterange.StatisticDateRangeType
import com.faltenreich.diaguard.statistic.distribution.GetStatisticDistributionUseCase
import com.faltenreich.diaguard.statistic.property.StatisticPropertyState
import com.faltenreich.diaguard.statistic.trend.GetStatisticTrendUseCase
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.quarter
import diaguard.shared.generated.resources.week
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
    private val getAverage: GetStatisticAverageUseCase,
    private val getTrend: GetStatisticTrendUseCase,
    private val getDistribution: GetStatisticDistributionUseCase,
    private val localization: Localization,
    private val dateTimeFormatter: DateTimeFormatter,
) : ViewModel<StatisticState, StatisticIntent, Unit>() {

    private val dateRangeType = MutableStateFlow(StatisticDateRangeType.WEEK)
    private val dateRange = MutableStateFlow(
        getToday().let { it.atStartOf(DateUnit.WEEK) .. it.atEndOf(DateUnit.WEEK) }
    )
    private val categories = MutableStateFlow(emptyList<MeasurementCategory.Local>())
    private val category = MutableStateFlow<MeasurementCategory.Local?>(null)
    private val properties = MutableStateFlow(emptyList<MeasurementProperty.Local>())
    private val property = MutableStateFlow<MeasurementProperty.Local?>(null)

    override val state = combine(
        dateRangeType,
        dateRange,
        category,
        property,
    ) { _, dateRange, category, property ->
        Triple(dateRange, category, property)
    }.flatMapLatest { (dateRange, category, property) ->
        val dateRangeState = StatisticDateRangeState(
            type = dateRangeType.value,
            dateRange = dateRange,
            title = when (dateRangeType.value) {
                StatisticDateRangeType.WEEK -> "%s %s".format(
                    localization.getString(Res.string.week),
                    dateTimeFormatter.formatWeek(dateRange.start),
                )
                StatisticDateRangeType.MONTH -> dateTimeFormatter.formatMonth(
                    month = dateRange.start.month,
                    abbreviated = false,
                )
                StatisticDateRangeType.QUARTER -> "%s %s".format(
                    localization.getString(Res.string.quarter),
                    dateTimeFormatter.formatQuarter(dateRange.start),
                )
                StatisticDateRangeType.YEAR -> dateTimeFormatter.formatYear(dateRange.start)
            },
            subtitle = dateTimeFormatter.formatDateRange(dateRange),
        )
        if (property != null) {
            combine(
                categories,
                properties,
                getAverage(property, dateRange),
                getTrend(property, dateRange),
                getDistribution(property, dateRange),
            ) { categories, properties, average, trend, distribution ->
                StatisticState(
                    dateRange = dateRangeState,
                    category = category?.let {
                        StatisticCategoryState(
                            categories = categories,
                            selection = category,
                        )
                    },
                    property = property.takeIf { properties.size > 1 || it.isNameUnique }?.let {
                        StatisticPropertyState(
                            properties = properties,
                            selection = property,
                        )
                    },
                    average = average,
                    trend = trend,
                    distribution = distribution,
                )
            }
        } else {
            flowOf(StatisticState(dateRangeState))
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
        scope.launch {
            dateRangeType.collectLatest {
                invalidateDateRange(dateRange.value.start)
            }
        }
    }

    override suspend fun handleIntent(intent: StatisticIntent) {
        when (intent) {
            is StatisticIntent.SetDateRangeType -> dateRangeType.update { intent.dateRangeType }
            is StatisticIntent.MoveDateRangeBack -> moveDateRange(value = -1)
            is StatisticIntent.MoveDateRangeForward -> moveDateRange(value = 1)
            is StatisticIntent.SetCategory -> category.update { intent.category }
            is StatisticIntent.SetProperty -> property.update { intent.property }
        }
    }

    private fun moveDateRange(value: Int) {
        val current = dateRange.value.start
        val date = when (dateRangeType.value) {
            StatisticDateRangeType.WEEK -> current.plus(value, DateUnit.WEEK)
            StatisticDateRangeType.MONTH -> current.plus(value, DateUnit.MONTH)
            StatisticDateRangeType.QUARTER -> current.plus(value, DateUnit.QUARTER)
            StatisticDateRangeType.YEAR -> current.plus(value, DateUnit.YEAR)
        }
        invalidateDateRange(date)
    }

    private fun invalidateDateRange(date: Date) {
        dateRange.update { dateRange ->
            val start = when (dateRangeType.value) {
                StatisticDateRangeType.WEEK -> date.atStartOf(DateUnit.WEEK)
                StatisticDateRangeType.MONTH -> date.atStartOf(DateUnit.MONTH)
                StatisticDateRangeType.QUARTER -> date.atStartOf(DateUnit.QUARTER)
                StatisticDateRangeType.YEAR -> date.atStartOf(DateUnit.YEAR)
            }
            val end = when (dateRangeType.value) {
                StatisticDateRangeType.WEEK -> start.atEndOf(DateUnit.WEEK)
                StatisticDateRangeType.MONTH -> start.atEndOf(DateUnit.MONTH)
                StatisticDateRangeType.QUARTER -> start.atEndOf(DateUnit.QUARTER)
                StatisticDateRangeType.YEAR -> start.atEndOf(DateUnit.YEAR)
            }
            dateRange.copy(start = start, endInclusive = end)
        }
    }
}