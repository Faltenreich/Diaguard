package com.faltenreich.diaguard.dashboard.hba1c

import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetLatestHbA1cUseCase(
    private val valueRepository: MeasurementValueRepository,
    private val dateTimeFactory: DateTimeFactory,
) {

    operator fun invoke(): Flow<MeasurementValue.Local?> {
        val now = dateTimeFactory.now()
        return valueRepository
            .observeLatestByProperty(DatabaseKey.MeasurementProperty.HBA1C)
            .map { value ->
                value?.takeIf {
                    val oneMonthAgo = now.date.minus(1, DateUnit.MONTH).atEndOfDay()
                    value.entry.dateTime > oneMonthAgo
                }
            }
    }
}