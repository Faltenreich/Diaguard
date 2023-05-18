package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class GetMeasurementDataUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
    private val measurementUnitRepository: MeasurementUnitRepository = inject(),
    private val measurementValueRepository: MeasurementValueRepository = inject(),
) {

    operator fun invoke(): Flow<MeasurementData> {
        return measurementPropertyRepository.getAll()
            .map { properties ->
                MeasurementData(properties = properties.map { property ->
                    // TODO: Implement functionally
                    val types = measurementTypeRepository.getByPropertyId(property.id).first()
                    MeasurementData.Property(
                        property = property,
                        values = types.map { type ->
                            // TODO: Pass selected unit and potential value
                            MeasurementData.Property.Value(
                                type = type,
                            )
                        }
                    )
                })
            }
    }
}