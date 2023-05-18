package com.faltenreich.diaguard.onboarding

import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementTypeUnitRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.first

class FirstStart(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
    private val measurementUnitRepository: MeasurementUnitRepository = inject(),
    private val measurementTypeUnitRepository: MeasurementTypeUnitRepository = inject(),
) {

    suspend operator fun invoke() {
        // TODO: Improve check
        val isEmpty = measurementPropertyRepository.getAll().first().isEmpty()
        if (!isEmpty) {
            return
        }
        val bloodSugarPropertyId = measurementPropertyRepository.create("Blood Sugar", icon = "\uD83E\uDE78")
        val bloodSugarTypeId = measurementTypeRepository.create("Blood Sugar", propertyId = bloodSugarPropertyId)
        val mgDlUnitId = measurementUnitRepository.create("mg/dL")
        measurementTypeUnitRepository.create(factor = 1.0, typeId = bloodSugarTypeId, unitId = mgDlUnitId)

        val insulinPropertyId = measurementPropertyRepository.create("Insulin", icon = "\uD83D\uDC89")
        val insulinBolusTypeId = measurementTypeRepository.create("Bolus", propertyId = insulinPropertyId)
        val insulinCorrectionTypeId = measurementTypeRepository.create("Correction", propertyId = insulinPropertyId)
        val insulinBasalTypeId = measurementTypeRepository.create("Basal", propertyId = insulinPropertyId)
        val ieUnitId = measurementUnitRepository.create("IE")
        measurementTypeUnitRepository.create(factor = 1.0, typeId = insulinBolusTypeId, unitId = ieUnitId)
        measurementTypeUnitRepository.create(factor = 1.0, typeId = insulinCorrectionTypeId, unitId = ieUnitId)
        measurementTypeUnitRepository.create(factor = 1.0, typeId = insulinBasalTypeId, unitId = ieUnitId)
    }
}