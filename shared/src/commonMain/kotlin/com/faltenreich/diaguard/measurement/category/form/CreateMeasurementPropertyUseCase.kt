package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange

class CreateMeasurementPropertyUseCase(
    private val propertyRepository: MeasurementPropertyRepository,
) {

    operator fun invoke(
        propertyName: String,
        propertySortIndex: Long,
        propertyAggregationStyle: MeasurementAggregationStyle,
        propertyRange: MeasurementValueRange,
        category: MeasurementCategory.Local,
        unit: MeasurementUnit.Local,
    ): MeasurementProperty.Local {
        val propertyId = propertyRepository.create(
            MeasurementProperty.User(
                name = propertyName,
                sortIndex = propertySortIndex,
                aggregationStyle = propertyAggregationStyle,
                range = propertyRange,
                category = category,
                unit = unit,
            )
        )
        return checkNotNull(propertyRepository.getById(propertyId))
    }
}