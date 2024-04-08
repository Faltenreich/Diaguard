package com.faltenreich.diaguard.dashboard.usecase

import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueForDatabase
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetAverageUseCase(
    private val measurementCategoryRepository: MeasurementCategoryRepository = inject(),
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
    private val measurementValueRepository: MeasurementValueRepository = inject(),
    private val mapMeasurementValue: MeasurementValueMapper = inject(),
    private val getToday: GetTodayUseCase = inject(),
) {

    operator fun invoke(): Flow<DashboardViewState.Revisit.Average?> {
        val category = measurementCategoryRepository.getBloodSugar()
        val categoryId = category.id

        val today = getToday()
        val todayAtEndOfDay = today.atEndOfDay()

        return combine(
            measurementTypeRepository.observeByCategoryId(categoryId),
            measurementValueRepository.observeAverageByCategoryId(
                categoryId = categoryId,
                minDateTime = today.atStartOfDay(),
                maxDateTime = todayAtEndOfDay,
            ),
            measurementValueRepository.observeAverageByCategoryId(
                categoryId = categoryId,
                minDateTime = today.minus(1, DateUnit.WEEK).atStartOfDay(),
                maxDateTime = todayAtEndOfDay,
            ),
            measurementValueRepository.observeAverageByCategoryId(
                categoryId = categoryId,
                minDateTime = today.minus(1, DateUnit.MONTH).atStartOfDay(),
                maxDateTime = todayAtEndOfDay,
            ),
        ) { types: List<MeasurementType>, averageOfDay: Double?, averageOfWeek: Double?, averageOfMonth: Double? ->
            val unit = types.first().selectedUnit
            DashboardViewState.Revisit.Average(
                day = averageOfDay?.let {
                    mapMeasurementValue(MeasurementValueForDatabase(averageOfDay, unit))
                }?.value,
                week = averageOfWeek?.let {
                    mapMeasurementValue(MeasurementValueForDatabase(averageOfWeek, unit))
                }?.value,
                month = averageOfMonth?.let {
                    mapMeasurementValue(MeasurementValueForDatabase(averageOfMonth, unit))
                }?.value,
            )
        }
    }
}