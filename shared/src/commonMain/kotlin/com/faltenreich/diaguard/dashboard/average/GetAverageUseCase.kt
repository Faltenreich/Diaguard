package com.faltenreich.diaguard.dashboard.average

import com.faltenreich.diaguard.dashboard.DashboardState
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.preference.DecimalPlaces
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
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
    private val getPreference: GetPreferenceUseCase,
) {

    operator fun invoke(): Flow<DashboardState.Average?> {
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
                getPreference(DecimalPlaces),
            ) { properties, averageOfDay, averageOfWeek, averageOfMonth, decimalPlaces ->
                val unit = properties.firstOrNull()?.selectedUnit ?: return@combine null
                DashboardState.Average(
                    day = averageOfDay?.let {
                        mapValue(
                            value = averageOfDay,
                            unit = unit,
                            decimalPlaces = decimalPlaces,
                        )
                    },
                    week = averageOfWeek?.let {
                        mapValue(
                            value = averageOfWeek,
                            unit = unit,
                            decimalPlaces = decimalPlaces,
                        )
                    },
                    month = averageOfMonth?.let {
                        mapValue(
                            value = averageOfMonth,
                            unit = unit,
                            decimalPlaces = decimalPlaces,
                        )
                    },
                )
            }
        }
    }
}