package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTimelineDataUseCase(
    private val valueRepository: MeasurementValueRepository = inject(),
    private val categoryRepository: MeasurementCategoryRepository = inject(),
) {

    operator fun invoke(date: Date): Flow<TimelineData> {
        return valueRepository.observeByDateRange(
            startDateTime = date.minus(2, DateUnit.DAY).atStartOfDay(),
            endDateTime = date.plus(2, DateUnit.DAY).atEndOfDay(),
        ).map { values ->
            val valuesForChart = values.filter { value -> value.property.category.isBloodSugar }
            val valuesForList = values.filterNot { value -> value.property.category.isBloodSugar }
            val categories = categoryRepository.getAll().filterNot(MeasurementCategory::isBloodSugar)
            TimelineData(
                chart = TimelineData.Chart(valuesForChart),
                table = TimelineData.Table(
                    categories = categories,
                    values = valuesForList,
                )
            )
        }
    }
}