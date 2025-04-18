package com.faltenreich.diaguard.backup.legacy.query

import com.faltenreich.diaguard.backup.legacy.query.measurement.ActivityLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.BloodPressureLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.BloodSugarLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.HbA1cLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.InsulinLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.MealLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.OxygenSaturationLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.PulseLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.WeightLegacyQueries
import com.faltenreich.diaguard.measurement.value.MeasurementValue

class MeasurementValueLegacyQueries(
    private val bloodSugarQueries: BloodSugarLegacyQueries,
    private val insulinQueries: InsulinLegacyQueries,
    private val mealQueries: MealLegacyQueries,
    private val activityQueries: ActivityLegacyQueries,
    private val hba1cQueries: HbA1cLegacyQueries,
    private val weightQueries: WeightLegacyQueries,
    private val pulseQueries: PulseLegacyQueries,
    private val bloodPressureQueries: BloodPressureLegacyQueries,
    private val oxygenSaturationQueries: OxygenSaturationLegacyQueries,
) : LegacyQueries<MeasurementValue.Legacy> {

    override fun getAll(): List<MeasurementValue.Legacy> {
        return listOf(
            bloodSugarQueries,
            insulinQueries,
            mealQueries,
            activityQueries,
            hba1cQueries,
            weightQueries,
            pulseQueries,
            bloodPressureQueries,
            oxygenSaturationQueries,
        ).flatMap(LegacyQueries<MeasurementValue.Legacy>::getAll)
    }
}