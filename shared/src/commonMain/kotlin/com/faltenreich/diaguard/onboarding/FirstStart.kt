package com.faltenreich.diaguard.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementTypeUnitRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.di.inject
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun FirstStart(
    modifier: Modifier = Modifier,
) {
    // TODO: Prevent subsequent calls on recomposition by wrapping inside a LaunchedEffect
    LoadData()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun LoadData(
    measurementPropertyRepository: MeasurementPropertyRepository = inject(),
    measurementTypeRepository: MeasurementTypeRepository = inject(),
    measurementUnitRepository: MeasurementUnitRepository = inject(),
    measurementTypeUnitRepository: MeasurementTypeUnitRepository = inject(),
) {
    // TODO: Extract into .csv and reduce duplication
    val bloodSugarPropertyId = measurementPropertyRepository.create(name = stringResource(MR.strings.blood_sugar), icon = "\uD83E\uDE78", sortIndex = 0)
    val mgDlUnitId = measurementUnitRepository.create(name = stringResource(MR.strings.mg_per_dl))
    val mmolLUnitId = measurementUnitRepository.create(name = stringResource(MR.strings.mmol_per_l))
    val bloodSugarTypeId = measurementTypeRepository.create(name = stringResource(MR.strings.blood_sugar), sortIndex = 0, propertyId = bloodSugarPropertyId)
    val bloodSugarTypeUnitId = measurementTypeUnitRepository.create(factor = 1.0, typeId = bloodSugarTypeId, unitId = mgDlUnitId)
    measurementTypeUnitRepository.create(factor = 0.0555, typeId = bloodSugarTypeId, unitId = mmolLUnitId)
    measurementTypeRepository.update(id = bloodSugarTypeId, name = stringResource(MR.strings.blood_sugar), sortIndex = 0, selectedTypeUnitId = bloodSugarTypeUnitId)

    val insulinPropertyId = measurementPropertyRepository.create(name = stringResource(MR.strings.insulin), icon = "\uD83D\uDC89", sortIndex = 1)
    val ieUnitId = measurementUnitRepository.create(name = stringResource(MR.strings.insulin_units))
    val insulinBolusTypeId = measurementTypeRepository.create(name = stringResource(MR.strings.bolus), sortIndex = 0, propertyId = insulinPropertyId)
    val insulinBolusTypeUnitId = measurementTypeUnitRepository.create(factor = 1.0, typeId = insulinBolusTypeId, unitId = ieUnitId)
    measurementTypeRepository.update(id = insulinBolusTypeId, name = stringResource(MR.strings.bolus), sortIndex = 0, selectedTypeUnitId = insulinBolusTypeUnitId)
    val insulinCorrectionTypeId = measurementTypeRepository.create(name = stringResource(MR.strings.correction), sortIndex = 1, propertyId = insulinPropertyId)
    val insulinCorrectionTypeUnitId = measurementTypeUnitRepository.create(factor = 1.0, typeId = insulinCorrectionTypeId, unitId = ieUnitId)
    measurementTypeRepository.update(id = insulinCorrectionTypeId, name = stringResource(MR.strings.correction), sortIndex = 1, selectedTypeUnitId = insulinCorrectionTypeUnitId)
    val insulinBasalTypeId = measurementTypeRepository.create(name = stringResource(MR.strings.basal), sortIndex = 2, propertyId = insulinPropertyId)
    val insulinBasalTypeUnitId = measurementTypeUnitRepository.create(factor = 1.0, typeId = insulinBasalTypeId, unitId = ieUnitId)
    measurementTypeRepository.update(id = insulinBasalTypeId, name = stringResource(MR.strings.basal), sortIndex = 2, selectedTypeUnitId = insulinBasalTypeUnitId)

    val mealPropertyId = measurementPropertyRepository.create(name = stringResource(MR.strings.meal), icon = "\uD83C\uDF5E", sortIndex = 2)
    val carbohydratesUnitId = measurementUnitRepository.create(name = stringResource(MR.strings.carbohydrates))
    val carbohydrateUnitsUnitId = measurementUnitRepository.create(name = stringResource(MR.strings.carbohydrate_units))
    val breadUnitsUnitId = measurementUnitRepository.create(name = stringResource(MR.strings.bread_units))
    val mealTypeId = measurementTypeRepository.create(name = stringResource(MR.strings.meal), sortIndex = 0, propertyId = mealPropertyId)
    val mealTypeUnitId = measurementTypeUnitRepository.create(factor = 1.0, typeId = mealTypeId, unitId = carbohydratesUnitId)
    measurementTypeRepository.update(id = mealTypeId, name = stringResource(MR.strings.meal), sortIndex = 0, selectedTypeUnitId = mealTypeUnitId)
    measurementTypeUnitRepository.create(factor = 0.1, typeId = mealTypeId, unitId = carbohydrateUnitsUnitId)
    measurementTypeUnitRepository.create(factor = 0.0833, typeId = mealTypeId, unitId = breadUnitsUnitId)

    val activityPropertyId = measurementPropertyRepository.create(name = stringResource(MR.strings.activity), icon = "\uD83C\uDFC3", sortIndex = 3)
    val minutesUnitId = measurementUnitRepository.create(name = stringResource(MR.strings.minutes))
    val activityTypeId = measurementTypeRepository.create(name = stringResource(MR.strings.activity), sortIndex = 0, propertyId = activityPropertyId)
    val activityTypeUnitId = measurementTypeUnitRepository.create(factor = 1.0, typeId = activityTypeId, unitId = minutesUnitId)
    measurementTypeRepository.update(id = activityTypeId, name = stringResource(MR.strings.activity), sortIndex = 0, activityTypeUnitId)
}