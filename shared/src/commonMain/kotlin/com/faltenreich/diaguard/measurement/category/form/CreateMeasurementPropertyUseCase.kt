package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange

class CreateMeasurementPropertyUseCase(
    private val propertyRepository: MeasurementPropertyRepository,
    private val unitRepository: MeasurementUnitRepository,
    private val dateTimeFactory: DateTimeFactory,
) {

    operator fun invoke(
        propertyName: String,
        propertySortIndex: Long,
        propertyAggregationStyle: MeasurementAggregationStyle,
        propertyRange: MeasurementValueRange,
        categoryId: Long,
        unitName: String,
    ): MeasurementProperty {
        val createdAt = dateTimeFactory.now()

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

        val unitId = unitRepository.create(
            createdAt = createdAt,
            updatedAt = createdAt,
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
            selectedUnitId = unitId,
        )

        return property.copy(selectedUnitId = unitId).apply {
            selectedUnit = MeasurementUnit(
                id = unitId,
                createdAt = createdAt,
                updatedAt = createdAt,
                key = null,
                name = unitName,
                abbreviation = unitAbbreviation,
                factor = unitFactor,
                propertyId = property.id,
            )
        }
    }
}