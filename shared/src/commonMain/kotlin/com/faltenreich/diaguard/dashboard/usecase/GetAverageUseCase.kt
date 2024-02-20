package com.faltenreich.diaguard.dashboard.usecase

import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.value.IntermediateValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueConverter
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.datetime.DateUnit
import com.faltenreich.diaguard.shared.datetime.GetTodayUseCase
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetAverageUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
    private val measurementValueRepository: MeasurementValueRepository = inject(),
    private val measurementValueConverter: MeasurementValueConverter = inject(),
    private val numberFormatter: NumberFormatter = inject(),
    private val getToday: GetTodayUseCase = inject(),
) {

    operator fun invoke(): Flow<DashboardViewState.Revisit.Average?> {
        val property = measurementPropertyRepository.getBloodSugar()
        val propertyId = property.id

        val today = getToday()
        val todayAtEndOfDay = today.atEndOfDay()

        return combine(
            measurementTypeRepository.observeByPropertyId(propertyId),
            measurementValueRepository.observeAverageByPropertyId(
                propertyId = propertyId,
                minDateTime = today.atStartOfDay(),
                maxDateTime = todayAtEndOfDay,
            ),
            measurementValueRepository.observeAverageByPropertyId(
                propertyId = propertyId,
                minDateTime = today.minus(1, DateUnit.WEEK).atStartOfDay(),
                maxDateTime = todayAtEndOfDay,
            ),
            measurementValueRepository.observeAverageByPropertyId(
                propertyId = propertyId,
                minDateTime = today.minus(1, DateUnit.MONTH).atStartOfDay(),
                maxDateTime = todayAtEndOfDay,
            ),
        ) { types: List<MeasurementType>, averageOfDay: Double?, averageOfWeek: Double?, averageOfMonth: Double? ->
            val unit = types.first().selectedUnit
            DashboardViewState.Revisit.Average(
                day = averageOfDay?.let {
                    measurementValueConverter.convertToCustom(IntermediateValue(averageOfDay, unit))
                }?.let(numberFormatter::invoke),
                week = averageOfWeek?.let {
                    measurementValueConverter.convertToCustom(IntermediateValue(averageOfWeek, unit))
                }?.let(numberFormatter::invoke),
                month = averageOfMonth?.let {
                    measurementValueConverter.convertToCustom(IntermediateValue(averageOfMonth, unit))
                }?.let(numberFormatter::invoke),
            )
        }
    }
}