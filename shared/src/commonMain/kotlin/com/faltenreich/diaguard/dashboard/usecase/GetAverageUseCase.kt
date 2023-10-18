package com.faltenreich.diaguard.dashboard.usecase

import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueFormatter
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.datetime.DateUnit
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetAverageUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
    private val measurementValueRepository: MeasurementValueRepository = inject(),
    private val measurementValueFormatter: MeasurementValueFormatter = inject(),
    private val dateTimeFactory: DateTimeFactory = inject(),
) {

    operator fun invoke(): Flow<DashboardViewState.Revisit.Average?> {
        val property = measurementPropertyRepository.getBloodSugar()
        val propertyId = property.id

        val today = dateTimeFactory.today()
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
            val factor = types.first().selectedUnit?.factor ?: throw IllegalStateException("Missing selected unit")
            DashboardViewState.Revisit.Average(
                day = averageOfDay?.let {
                    measurementValueFormatter.formatValue(
                        value = averageOfDay,
                        factor = factor,
                    )
                },
                week = averageOfWeek?.let {
                    measurementValueFormatter.formatValue(
                        value = averageOfWeek,
                        factor = factor,
                    )
                },
                month = averageOfMonth?.let {
                    measurementValueFormatter.formatValue(
                        value = averageOfMonth,
                        factor = factor,
                    )
                },
            )
        }
    }
}