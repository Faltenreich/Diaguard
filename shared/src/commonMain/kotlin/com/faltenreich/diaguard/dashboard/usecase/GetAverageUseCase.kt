package com.faltenreich.diaguard.dashboard.usecase

import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueFormatter
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetAverageUseCase(
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
    private val measurementValueRepository: MeasurementValueRepository = inject(),
    private val measurementValueFormatter: MeasurementValueFormatter = inject(),
) {

    operator fun invoke(): Flow<DashboardViewState.Revisit.Average?> {
        val propertyId = MeasurementProperty.BLOOD_SUGAR_ID

        val today = Date.today()
        // TODO: Subtract/add weeks and months
        val todayAtStartOfDay = today.atTime(Time.atStartOfDay())
        val todayAtEndOfDay = today.atTime(Time.atEndOfDay())

        return combine(
            measurementTypeRepository.observeByPropertyId(propertyId),
            measurementValueRepository.observeAverageByPropertyId(
                propertyId = propertyId,
                minDateTime = todayAtStartOfDay,
                maxDateTime = todayAtEndOfDay,
            ),
            measurementValueRepository.observeAverageByPropertyId(
                propertyId = propertyId,
                minDateTime = todayAtStartOfDay,
                maxDateTime = todayAtEndOfDay,
            ),
            measurementValueRepository.observeAverageByPropertyId(
                propertyId = propertyId,
                minDateTime = todayAtStartOfDay,
                maxDateTime = todayAtEndOfDay,
            ),
        ) { types: List<MeasurementType>, averageOfDay: Double?, averageOfWeek: Double?, averageOfMonth: Double? ->
            val factor = types.first().selectedUnit?.factor ?: return@combine null
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