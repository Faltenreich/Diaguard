package com.faltenreich.diaguard.timeline.canvas.table

import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateUnit
import kotlinx.coroutines.flow.Flow

class GetTimelineTableMeasurementValuesUseCase(
    private val repository: MeasurementValueRepository,
) {

    operator fun invoke(date: Date): Flow<List<MeasurementValue.Local>> {
        return repository.observeByDateRangeIfCategoryIsActive(
            startDateTime = date.minus(1, DateUnit.DAY).atStartOfDay(),
            endDateTime = date.plus(1, DateUnit.DAY).atEndOfDay(),
            excludedPropertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
        )
    }
}