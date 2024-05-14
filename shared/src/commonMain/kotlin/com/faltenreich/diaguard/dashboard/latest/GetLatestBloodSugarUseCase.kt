package com.faltenreich.diaguard.dashboard.latest

import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.measurement.value.tint.GetMeasurementValueTintUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GetLatestBloodSugarUseCase(
    private val categoryRepository: MeasurementCategoryRepository,
    private val valueRepository: MeasurementValueRepository,
    private val valueMapper: MeasurementValueMapper,
    private val getValueColor: GetMeasurementValueTintUseCase,
    private val dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(): Flow<DashboardViewState.Revisit.LatestBloodSugar?> {
        return categoryRepository.observeBloodSugar().flatMapLatest { category ->
            val categoryId = category?.id ?: return@flatMapLatest flowOf(null)
            valueRepository.observeLatestByCategoryId(categoryId).map { value ->
                when (value) {
                    null -> null
                    else -> DashboardViewState.Revisit.LatestBloodSugar(
                        entry = value.entry,
                        value = valueMapper(value).value,
                        tint = getValueColor(value),
                        timePassed = dateTimeFormatter.formatTimePassed(
                            start = value.entry.dateTime,
                            end = dateTimeFactory.now(),
                        ),
                    )
                }
            }
        }
    }
}