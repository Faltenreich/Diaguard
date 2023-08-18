package com.faltenreich.diaguard.dashboard.usecase

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow

class GetLatestBloodSugarUseCase(
    private val measurementValueRepository: MeasurementValueRepository = inject(),
) {

    operator fun invoke(): Flow<MeasurementValue?> {
        return measurementValueRepository.observeLatestByPropertyId(
            propertyId = MeasurementProperty.BLOOD_SUGAR_ID,
        )
    }
}