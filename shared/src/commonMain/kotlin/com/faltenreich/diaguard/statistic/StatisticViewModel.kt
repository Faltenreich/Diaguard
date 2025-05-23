package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.datetime.format.FormatDateTimeUseCase
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.usecase.GetActiveMeasurementCategoriesUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.statistic.average.GetAverageUseCase
import com.faltenreich.diaguard.statistic.distribution.GetDistributionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest

class StatisticViewModel(
    getToday: GetTodayUseCase,
    getCategories: GetActiveMeasurementCategoriesUseCase,
    private val formatDateRange: FormatDateTimeUseCase,
    private val getAverage: GetAverageUseCase,
    private val getDistribution: GetDistributionUseCase,
) : ViewModel<StatisticState, StatisticIntent, Unit>() {

    private val category = MutableStateFlow<MeasurementCategory.Local?>(null)
    val dateRange = MutableStateFlow(getToday().let { it.minus(1, DateUnit.WEEK) .. it })

    override val state = combine(
        category,
        dateRange,
        getCategories(),
    ) { category, dateRange, categories ->
        Triple(category ?: categories.first(), dateRange, categories)
    }.flatMapLatest { (category, dateRange, categories) ->
        // FIXME: NullPointerException when changing dateRange
        combine(
            getAverage(category, dateRange),
            getDistribution(category, dateRange),
        ) { average, distribution ->
            StatisticState(
                dateRange = formatDateRange(dateRange),
                category = category,
                categories = categories,
                average = average,
                distribution = distribution,
            )
        }
    }

    override suspend fun handleIntent(intent: StatisticIntent) {
        when (intent) {
            is StatisticIntent.SetCategory -> category.value = intent.category
        }
    }
}