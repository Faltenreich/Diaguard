package com.faltenreich.diaguard.dashboard.hba1c

import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

class GetEstimatedHbA1cUseCase(
    private val valueRepository: MeasurementValueRepository,
    private val propertyRepository: MeasurementPropertyRepository,
    private val dateTimeFactory: DateTimeFactory,
) {

    operator fun invoke(): Flow<MeasurementValue.Average?> {
        val now = dateTimeFactory.now()
        return combine(
            valueRepository.observeAverageByPropertyKey(
                propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
                minDateTime = now.date.minus(1, DateUnit.QUARTER).atStartOfDay(),
                maxDateTime = now,
            ),
            // TODO: Observe by Key
            flowOf(propertyRepository.getByKey(DatabaseKey.MeasurementProperty.HBA1C.key)),
        ) { bloodSugarAverage, hbA1cProperty ->
            bloodSugarAverage?.value ?: return@combine null
            MeasurementValue.Average(
                value = 0.031 * bloodSugarAverage.value + 2.393,
                property = hbA1cProperty,
            )
        }
    }
}