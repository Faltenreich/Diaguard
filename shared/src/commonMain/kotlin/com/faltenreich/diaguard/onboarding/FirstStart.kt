package com.faltenreich.diaguard.onboarding

import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.first

class FirstStart(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
    private val measurementUnitRepository: MeasurementUnitRepository = inject(),
) {

    suspend operator fun invoke() {
        // TODO: Improve check
        val isEmpty = measurementPropertyRepository.getAll().first().isEmpty()
        if (!isEmpty) {
            return
        }
        val bloodSugarPropertyId = measurementPropertyRepository.create("Blood Sugar")
        val bloodSugarTypeId = measurementTypeRepository.create("Blood Sugar", propertyId = bloodSugarPropertyId)
        measurementUnitRepository.create("mg/dL", factor = 1.0, typeId = bloodSugarTypeId)

        val insulinPropertyId = measurementPropertyRepository.create("Insulin")
        val insulinBolusTypeId = measurementTypeRepository.create("Bolus", propertyId = insulinPropertyId)
        val insulinCorrectionTypeId = measurementTypeRepository.create("Correction", propertyId = insulinPropertyId)
        val insulinBasalTypeId = measurementTypeRepository.create("Basal", propertyId = insulinPropertyId)
        // TODO: Reuse units
        measurementUnitRepository.create("IE", factor = 1.0, typeId = insulinBolusTypeId)
        measurementUnitRepository.create("IE", factor = 1.0, typeId = insulinCorrectionTypeId)
        measurementUnitRepository.create("IE", factor = 1.0, typeId = insulinBasalTypeId)
    }
}