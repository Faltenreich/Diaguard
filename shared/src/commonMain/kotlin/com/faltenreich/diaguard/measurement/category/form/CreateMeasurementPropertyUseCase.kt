package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange

class CreateMeasurementPropertyUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository,
    private val measurementUnitRepository: MeasurementUnitRepository,
) {

    operator fun invoke(
        propertyKey: String?,
        propertyName: String,
        propertySortIndex: Long,
        propertyAggregationStyle: MeasurementAggregationStyle,
        propertyRange: MeasurementValueRange,
        categoryId: Long,
        unitKey: String?,
        unitName: String,
    ) {
        val propertyId = measurementPropertyRepository.create(
            key = propertyKey,
            name = propertyName,
            sortIndex = propertySortIndex,
            aggregationStyle = propertyAggregationStyle,
            range = propertyRange,
            categoryId = categoryId,
        )
        val unitId = measurementUnitRepository.create(
            key = unitKey,
            name = unitName,
            abbreviation = unitName, // TODO: Make customizable?
            factor = MeasurementUnit.FACTOR_DEFAULT,
            propertyId = propertyId,
        )
        measurementPropertyRepository.update(
            id = propertyId,
            name = propertyName,
            sortIndex = propertySortIndex,
            aggregationStyle = propertyAggregationStyle,
            range = propertyRange,
            selectedUnitId = unitId,
        )
    }
}