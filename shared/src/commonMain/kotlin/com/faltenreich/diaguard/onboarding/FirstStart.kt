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
) {
    val bloodSugarPropertyId = measurementPropertyRepository.create(name = stringResource(MR.strings.blood_sugar), icon = "\uD83E\uDE78", sortIndex = 0, isUserGenerated = false)
    val bloodSugarTypeId = measurementTypeRepository.create(name = stringResource(MR.strings.blood_sugar), sortIndex = 0, propertyId = bloodSugarPropertyId)
    val mgPerDlUnitId = measurementUnitRepository.create(name = stringResource(MR.strings.mg_per_dl), factor = 1.0, typeId = bloodSugarTypeId)
    measurementTypeRepository.update(id = bloodSugarTypeId, name = stringResource(MR.strings.blood_sugar), sortIndex = 0, selectedUnitId = mgPerDlUnitId)
    measurementUnitRepository.create(name = stringResource(MR.strings.mmol_per_l), factor = 0.0555, typeId = bloodSugarTypeId)

    val insulinPropertyId = measurementPropertyRepository.create(name = stringResource(MR.strings.insulin), icon = "\uD83D\uDC89", sortIndex = 1, isUserGenerated = false)
    val insulinBolusTypeId = measurementTypeRepository.create(name = stringResource(MR.strings.bolus), sortIndex = 0, propertyId = insulinPropertyId)
    val insulinBolusUnitId = measurementUnitRepository.create(name = stringResource(MR.strings.insulin_units), factor = 1.0, typeId = insulinBolusTypeId)
    measurementTypeRepository.update(id = insulinBolusTypeId, name = stringResource(MR.strings.bolus), sortIndex = 0, selectedUnitId = insulinBolusUnitId)
    val insulinCorrectionTypeId = measurementTypeRepository.create(name = stringResource(MR.strings.correction), sortIndex = 1, propertyId = insulinPropertyId)
    val insulinCorrectionUnitId = measurementUnitRepository.create(name = stringResource(MR.strings.insulin_units), factor = 1.0, typeId = insulinCorrectionTypeId)
    measurementTypeRepository.update(id = insulinCorrectionTypeId, name = stringResource(MR.strings.correction), sortIndex = 1, selectedUnitId = insulinCorrectionUnitId)
    val insulinBasalTypeId = measurementTypeRepository.create(name = stringResource(MR.strings.basal), sortIndex = 2, propertyId = insulinPropertyId)
    val insulinBasalUnitId = measurementUnitRepository.create(name = stringResource(MR.strings.insulin_units), factor = 1.0, typeId = insulinBasalTypeId)
    measurementTypeRepository.update(id = insulinBasalTypeId, name = stringResource(MR.strings.basal), sortIndex = 2, selectedUnitId = insulinBasalUnitId)

    val mealPropertyId = measurementPropertyRepository.create(name = stringResource(MR.strings.meal), icon = "\uD83C\uDF5E", sortIndex = 2, isUserGenerated = false)
    val mealTypeId = measurementTypeRepository.create(name = stringResource(MR.strings.meal), sortIndex = 0, propertyId = mealPropertyId)
    val carbohydratesUnitId = measurementUnitRepository.create(name = stringResource(MR.strings.carbohydrates), factor = 1.0, typeId = mealTypeId)
    measurementTypeRepository.update(id = mealTypeId, name = stringResource(MR.strings.meal), sortIndex = 0, selectedUnitId = carbohydratesUnitId)
    measurementUnitRepository.create(name = stringResource(MR.strings.carbohydrate_units), factor = 0.1, typeId = mealTypeId)
    measurementUnitRepository.create(name = stringResource(MR.strings.bread_units), factor = 0.0833, typeId = mealTypeId)

    val activityPropertyId = measurementPropertyRepository.create(name = stringResource(MR.strings.activity), icon = "\uD83C\uDFC3", sortIndex = 3, isUserGenerated = false)
    val activityTypeId = measurementTypeRepository.create(name = stringResource(MR.strings.activity), sortIndex = 0, propertyId = activityPropertyId)
    val minutesUnitId = measurementUnitRepository.create(name = stringResource(MR.strings.minutes), factor = 1.0, typeId = activityTypeId)
    measurementTypeRepository.update(id = activityTypeId, name = stringResource(MR.strings.activity), sortIndex = 0, selectedUnitId = minutesUnitId)
}