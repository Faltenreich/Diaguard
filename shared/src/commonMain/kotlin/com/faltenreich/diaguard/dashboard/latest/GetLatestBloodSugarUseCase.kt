package com.faltenreich.diaguard.dashboard.latest

import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.measurement.value.tint.GetMeasurementValueTintUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetLatestBloodSugarUseCase(
    private val measurementCategoryRepository: MeasurementCategoryRepository,
    private val measurementValueRepository: MeasurementValueRepository,
    private val measurementValueMapper: MeasurementValueMapper,
    private val getMeasurementValueColor: GetMeasurementValueTintUseCase,
    private val dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(): Flow<DashboardViewState.Revisit.LatestBloodSugar?> {
        val category = measurementCategoryRepository.getBloodSugar()
        return measurementValueRepository.observeLatestByCategoryId(category.id).map { value ->
            when (value) {
                null -> null
                else -> DashboardViewState.Revisit.LatestBloodSugar(
                    entry = value.entry,
                    value = measurementValueMapper(value).value,
                    tint = getMeasurementValueColor(value),
                    timePassed = dateTimeFormatter.formatTimePassed(
                        start = value.entry.dateTime,
                        end = dateTimeFactory.now(),
                    ),
                )
            }
        }
    }
}