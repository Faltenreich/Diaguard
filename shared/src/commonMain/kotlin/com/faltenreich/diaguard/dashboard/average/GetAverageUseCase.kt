package com.faltenreich.diaguard.dashboard.average

import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueForDatabase
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class GetAverageUseCase(
    private val categoryRepository: MeasurementCategoryRepository,
    private val propertyRepository: MeasurementPropertyRepository,
    private val valueRepository: MeasurementValueRepository,
    private val mapValue: MeasurementValueMapper,
    private val getToday: GetTodayUseCase,
) {

    operator fun invoke(): Flow<DashboardViewState.Revisit.Average?> {
        val today = getToday()
        val todayAtEndOfDay = today.atEndOfDay()

        return categoryRepository.observeBloodSugar().flatMapLatest { category ->
            val categoryId = category?.id ?: return@flatMapLatest flowOf(null)
            combine(
                propertyRepository.observeByCategoryId(categoryId),
                valueRepository.observeAverageByCategoryId(
                    categoryId = categoryId,
                    minDateTime = today.atStartOfDay(),
                    maxDateTime = todayAtEndOfDay,
                ),
                valueRepository.observeAverageByCategoryId(
                    categoryId = categoryId,
                    minDateTime = today.minus(1, DateUnit.WEEK).atStartOfDay(),
                    maxDateTime = todayAtEndOfDay,
                ),
                valueRepository.observeAverageByCategoryId(
                    categoryId = categoryId,
                    minDateTime = today.minus(1, DateUnit.MONTH).atStartOfDay(),
                    maxDateTime = todayAtEndOfDay,
                ),
            ) { properties, averageOfDay, averageOfWeek, averageOfMonth ->
                val unit = properties.first().selectedUnit
                DashboardViewState.Revisit.Average(
                    day = averageOfDay?.let {
                        mapValue(MeasurementValueForDatabase(averageOfDay, unit))
                    }?.value,
                    week = averageOfWeek?.let {
                        mapValue(MeasurementValueForDatabase(averageOfWeek, unit))
                    }?.value,
                    month = averageOfMonth?.let {
                        mapValue(MeasurementValueForDatabase(averageOfMonth, unit))
                    }?.value,
                )
            }
        }
    }
}