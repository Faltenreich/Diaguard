package com.faltenreich.diaguard.backup.mock

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.Import
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString

class MockImport(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
    private val measurementUnitRepository: MeasurementUnitRepository = inject(),
) : Import {

    override operator fun invoke() {
        val bloodSugarPropertyId = measurementPropertyRepository.create(name = getString(MR.strings.blood_sugar), key = MeasurementProperty.PredefinedKey.BLOOD_SUGAR, icon = "\uD83E\uDE78", sortIndex = 0)
        val bloodSugarTypeId = measurementTypeRepository.create(name = getString(MR.strings.blood_sugar), sortIndex = 0, propertyId = bloodSugarPropertyId)
        val mgPerDlUnitId = measurementUnitRepository.create(name = getString(MR.strings.mg_per_dl), factor = 1.0, typeId = bloodSugarTypeId)
        measurementTypeRepository.update(id = bloodSugarTypeId, name = getString(MR.strings.blood_sugar), sortIndex = 0, selectedUnitId = mgPerDlUnitId)
        measurementUnitRepository.create(name = getString(MR.strings.mmol_per_l), factor = 0.0555, typeId = bloodSugarTypeId)

        val insulinPropertyId = measurementPropertyRepository.create(name = getString(MR.strings.insulin), key = MeasurementProperty.PredefinedKey.INSULIN, icon = "\uD83D\uDC89", sortIndex = 1)
        val insulinBolusTypeId = measurementTypeRepository.create(name = getString(MR.strings.bolus), sortIndex = 0, propertyId = insulinPropertyId)
        val insulinBolusUnitId = measurementUnitRepository.create(name = getString(MR.strings.insulin_units), factor = 1.0, typeId = insulinBolusTypeId)
        measurementTypeRepository.update(id = insulinBolusTypeId, name = getString(MR.strings.bolus), sortIndex = 0, selectedUnitId = insulinBolusUnitId)
        val insulinCorrectionTypeId = measurementTypeRepository.create(name = getString(MR.strings.correction), sortIndex = 1, propertyId = insulinPropertyId)
        val insulinCorrectionUnitId = measurementUnitRepository.create(name = getString(MR.strings.insulin_units), factor = 1.0, typeId = insulinCorrectionTypeId)
        measurementTypeRepository.update(id = insulinCorrectionTypeId, name = getString(MR.strings.correction), sortIndex = 1, selectedUnitId = insulinCorrectionUnitId)
        val insulinBasalTypeId = measurementTypeRepository.create(name = getString(MR.strings.basal), sortIndex = 2, propertyId = insulinPropertyId)
        val insulinBasalUnitId = measurementUnitRepository.create(name = getString(MR.strings.insulin_units), factor = 1.0, typeId = insulinBasalTypeId)
        measurementTypeRepository.update(id = insulinBasalTypeId, name = getString(MR.strings.basal), sortIndex = 2, selectedUnitId = insulinBasalUnitId)

        val mealPropertyId = measurementPropertyRepository.create(name = getString(MR.strings.meal), key = MeasurementProperty.PredefinedKey.MEAL, icon = "\uD83C\uDF5E", sortIndex = 2)
        val mealTypeId = measurementTypeRepository.create(name = getString(MR.strings.meal), sortIndex = 0, propertyId = mealPropertyId)
        val carbohydratesUnitId = measurementUnitRepository.create(name = getString(MR.strings.carbohydrates), factor = 1.0, typeId = mealTypeId)
        measurementTypeRepository.update(id = mealTypeId, name = getString(MR.strings.meal), sortIndex = 0, selectedUnitId = carbohydratesUnitId)
        measurementUnitRepository.create(name = getString(MR.strings.carbohydrate_units), factor = 0.1, typeId = mealTypeId)
        measurementUnitRepository.create(name = getString(MR.strings.bread_units), factor = 0.0833, typeId = mealTypeId)

        val activityPropertyId = measurementPropertyRepository.create(name = getString(MR.strings.activity), key = MeasurementProperty.PredefinedKey.ACTIVITY, icon = "\uD83C\uDFC3", sortIndex = 3)
        val activityTypeId = measurementTypeRepository.create(name = getString(MR.strings.activity), sortIndex = 0, propertyId = activityPropertyId)
        val minutesUnitId = measurementUnitRepository.create(name = getString(MR.strings.minutes), factor = 1.0, typeId = activityTypeId)
        measurementTypeRepository.update(id = activityTypeId, name = getString(MR.strings.activity), sortIndex = 0, selectedUnitId = minutesUnitId)

        val hbA1cPropertyId = measurementPropertyRepository.create(name = getString(MR.strings.hba1c), key = MeasurementProperty.PredefinedKey.HBA1C, icon = "%", sortIndex = 4)
        val hbA1cTypeId = measurementTypeRepository.create(name = getString(MR.strings.hba1c), sortIndex = 0, propertyId = hbA1cPropertyId)
        val hbA1cPercentUnitId = measurementUnitRepository.create(name = getString(MR.strings.percent), factor = 1.0, typeId = hbA1cTypeId)
        measurementUnitRepository.create(name = getString(MR.strings.mmol_per_mol), factor = 0.00001, typeId = hbA1cTypeId)
        measurementTypeRepository.update(id = hbA1cTypeId, name = getString(MR.strings.hba1c), sortIndex = 0, selectedUnitId = hbA1cPercentUnitId)

        val weightPropertyId = measurementPropertyRepository.create(name = getString(MR.strings.weight), key = MeasurementProperty.PredefinedKey.WEIGHT, icon = "\uD83C\uDFCB", sortIndex = 5)
        val weightTypeId = measurementTypeRepository.create(name = getString(MR.strings.weight), sortIndex = 0, propertyId = weightPropertyId)
        val weightKilogramUnitId = measurementUnitRepository.create(name = getString(MR.strings.kilogram), factor = 1.0, typeId = weightTypeId)
        measurementUnitRepository.create(name = getString(MR.strings.pound), factor = 2.20462262185, typeId = weightTypeId)
        measurementTypeRepository.update(id = weightTypeId, name = getString(MR.strings.weight), sortIndex = 0, selectedUnitId = weightKilogramUnitId)

        val pulsePropertyId = measurementPropertyRepository.create(name = getString(MR.strings.pulse), key = MeasurementProperty.PredefinedKey.PULSE, icon = "\uD83D\uDC9A", sortIndex = 6)
        val pulseTypeId = measurementTypeRepository.create(name = getString(MR.strings.pulse), sortIndex = 0, propertyId = pulsePropertyId)
        val pulseBpmUnitId = measurementUnitRepository.create(name = getString(MR.strings.bpm), factor = 1.0, typeId = pulseTypeId)
        measurementTypeRepository.update(id = pulseTypeId, name = getString(MR.strings.pulse), sortIndex = 0, selectedUnitId = pulseBpmUnitId)

        val pressurePropertyId = measurementPropertyRepository.create(name = getString(MR.strings.pressure), key = MeasurementProperty.PredefinedKey.BLOOD_PRESSURE, icon = "⛽", sortIndex = 7)
        val pressureSystolicTypeId = measurementTypeRepository.create(name = getString(MR.strings.systolic), sortIndex = 0, propertyId = pressurePropertyId)
        val pressureSystolicUnitId = measurementUnitRepository.create(name = getString(MR.strings.mmhg), factor = 1.0, typeId = pressureSystolicTypeId)
        measurementTypeRepository.update(id = pressureSystolicTypeId, name = getString(MR.strings.systolic), sortIndex = 0, selectedUnitId = pressureSystolicUnitId)
        val pressureDiastolicTypeId = measurementTypeRepository.create(name = getString(MR.strings.diastolic), sortIndex = 0, propertyId = pressurePropertyId)
        val pressureDiastolicUnitId = measurementUnitRepository.create(name = getString(MR.strings.mmhg), factor = 1.0, typeId = pressureDiastolicTypeId)
        measurementTypeRepository.update(id = pressureDiastolicTypeId, name = getString(MR.strings.diastolic), sortIndex = 0, selectedUnitId = pressureDiastolicUnitId)

        val oxygenSaturationPropertyId = measurementPropertyRepository.create(name = getString(MR.strings.oxygen_saturation), key = MeasurementProperty.PredefinedKey.OXYGEN_SATURATION, icon = "O²", sortIndex = 8)
        val oxygenSaturationTypeId = measurementTypeRepository.create(name = getString(MR.strings.oxygen_saturation), sortIndex = 0, propertyId = oxygenSaturationPropertyId)
        val oxygenSaturationUnitId = measurementUnitRepository.create(name = getString(MR.strings.percent), factor = 1.0, typeId = oxygenSaturationTypeId)
        measurementTypeRepository.update(id = oxygenSaturationTypeId, name = getString(MR.strings.oxygen_saturation), sortIndex = 0, selectedUnitId = oxygenSaturationUnitId)
    }
}