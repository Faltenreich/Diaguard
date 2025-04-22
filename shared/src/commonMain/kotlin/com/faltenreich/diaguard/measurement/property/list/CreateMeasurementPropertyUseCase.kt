package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange

class CreateMeasurementPropertyUseCase(private val repository: MeasurementPropertyRepository) {

    operator fun invoke(
        name: String,
        sortIndex: Long,
        aggregationStyle: MeasurementAggregationStyle,
        range: MeasurementValueRange,
        category: MeasurementCategory.Local,
        unit: MeasurementUnit.Local,
    ): MeasurementProperty.Local {
        val id = repository.create(
            MeasurementProperty.User(
                name = name,
                sortIndex = sortIndex,
                aggregationStyle = aggregationStyle,
                range = range,
                category = category,
                unit = unit,
            )
        )
        return checkNotNull(repository.getById(id))
    }
}