package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import kotlinx.coroutines.flow.Flow

class GetMeasurementValuesAroundDateUseCase(
    private val valueRepository: MeasurementValueRepository,
) {

    operator fun invoke(date: Date): Flow<List<MeasurementValue>> {
        return valueRepository.observeByDateRange(
            startDateTime = date.minus(2, DateUnit.DAY).atStartOfDay(),
            endDateTime = date.plus(2, DateUnit.DAY).atEndOfDay(),
        )
    }
}