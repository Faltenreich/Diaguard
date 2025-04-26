package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

class StoreMeasurementPropertyUseCase(private val repository: MeasurementPropertyRepository) {

    operator fun invoke(
        property: MeasurementProperty.User,
        unit: MeasurementUnit.Local,
    ): MeasurementProperty.Local {
        val id = repository.create(
            property = MeasurementProperty.User(
                name = property.name,
                sortIndex = property.sortIndex,
                aggregationStyle = property.aggregationStyle,
                range =property. range,
                category = property.category,
            ),
            unit = unit,
        )
        return checkNotNull(repository.getById(id))
    }

    operator fun invoke(property: MeasurementProperty.Local): MeasurementProperty.Local {
        repository.update(property)
        return property
    }
}