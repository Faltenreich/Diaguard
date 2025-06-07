package com.faltenreich.diaguard.measurement.value.usecase

import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import kotlinx.coroutines.flow.Flow

class GetMeasurementValuesInDateRangeUseCase(
    private val repository: MeasurementValueRepository,
) {

    operator fun invoke(dateRange: DateRange): Flow<List<MeasurementValue.Local>> {
        return repository.observeByDateRange(
            startDateTime = dateRange.start.atStartOfDay(),
            endDateTime = dateRange.endInclusive.atEndOfDay(),
            propertyKey = TODO(),
        )
    }
}