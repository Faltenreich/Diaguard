package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow

class GetMeasurementTypesUseCase(
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
) {

    operator fun invoke(property: MeasurementProperty): Flow<List<MeasurementType>> {
        return measurementTypeRepository.observeByPropertyId(property.id)
    }
}