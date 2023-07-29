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
    // TODO: Prevent subsequent calls on recomposition
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
    val bloodSugarPropertyId = measurementPropertyRepository.create(stringResource(MR.strings.blood_sugar), icon = "\uD83E\uDE78")
    val mgDlUnitId = measurementUnitRepository.create(stringResource(MR.strings.mg_per_dl))
    val bloodSugarTypeId = measurementTypeRepository.create(stringResource(MR.strings.blood_sugar), selectedUnitId = mgDlUnitId, propertyId = bloodSugarPropertyId)
    measurementTypeUnitRepository.create(factor = 1.0, typeId = bloodSugarTypeId, unitId = mgDlUnitId)

    val insulinPropertyId = measurementPropertyRepository.create(stringResource(MR.strings.insulin), icon = "\uD83D\uDC89")
    val ieUnitId = measurementUnitRepository.create(stringResource(MR.strings.insulin_units))
    val insulinBolusTypeId = measurementTypeRepository.create(stringResource(MR.strings.bolus), selectedUnitId = ieUnitId, propertyId = insulinPropertyId)
    val insulinCorrectionTypeId = measurementTypeRepository.create(stringResource(MR.strings.correction), selectedUnitId = ieUnitId, propertyId = insulinPropertyId)
    val insulinBasalTypeId = measurementTypeRepository.create(stringResource(MR.strings.basal), selectedUnitId = ieUnitId, propertyId = insulinPropertyId)
    measurementTypeUnitRepository.create(factor = 1.0, typeId = insulinBolusTypeId, unitId = ieUnitId)
    measurementTypeUnitRepository.create(factor = 1.0, typeId = insulinCorrectionTypeId, unitId = ieUnitId)
    measurementTypeUnitRepository.create(factor = 1.0, typeId = insulinBasalTypeId, unitId = ieUnitId)

    val mealPropertyId = measurementPropertyRepository.create(stringResource(MR.strings.meal), icon = "\uD83C\uDF5E")
    val carbohydratesUnitId = measurementUnitRepository.create(stringResource(MR.strings.carbohydrates))
    val mealTypeId = measurementTypeRepository.create(stringResource(MR.strings.meal), selectedUnitId = carbohydratesUnitId, propertyId = mealPropertyId)
    measurementTypeUnitRepository.create(factor = 1.0, typeId = mealTypeId, unitId = carbohydratesUnitId)

    val activityPropertyId = measurementPropertyRepository.create(stringResource(MR.strings.activity), icon = "\uD83C\uDFC3")
    val minutesUnitId = measurementUnitRepository.create(stringResource(MR.strings.minutes))
    val activityTypeId = measurementTypeRepository.create(stringResource(MR.strings.activity), selectedUnitId = minutesUnitId, propertyId = activityPropertyId)
    measurementTypeUnitRepository.create(factor = 1.0, typeId = activityTypeId, unitId = minutesUnitId)
}