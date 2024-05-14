package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange

class CreateMeasurementPropertyUseCase(
    private val propertyRepository: MeasurementPropertyRepository,
    private val unitRepository: MeasurementUnitRepository,
) {

    operator fun invoke(
        propertyName: String,
        propertySortIndex: Long,
        propertyAggregationStyle: MeasurementAggregationStyle,
        propertyRange: MeasurementValueRange,
        categoryId: Long,
        unitName: String,
    ): MeasurementProperty {
        val unitFactor = MeasurementUnit.FACTOR_DEFAULT
        // TODO: Make customizable?
        val unitAbbreviation = unitName

        val property = propertyRepository.create(
            key = null,
            name = propertyName,
            sortIndex = propertySortIndex,
            aggregationStyle = propertyAggregationStyle,
            range = propertyRange,
            categoryId = categoryId,
        )

        val unit = unitRepository.create(
            key = null,
            name = unitName,
            abbreviation = unitAbbreviation,
            factor = unitFactor,
            propertyId = property.id,
        )

        propertyRepository.update(
            id = property.id,
            name = propertyName,
            sortIndex = propertySortIndex,
            aggregationStyle = propertyAggregationStyle,
            range = propertyRange,
            selectedUnitId = unit.id,
        )

        return property
            .copy(selectedUnitId = unit.id)
            .apply { selectedUnit = unit }
    }
}