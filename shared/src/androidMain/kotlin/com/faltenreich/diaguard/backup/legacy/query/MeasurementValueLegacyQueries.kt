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
    private val bloodSugarDao: BloodSugarLegacyQueries,
    private val insulinDao: InsulinLegacyQueries,
    private val mealDao: MealLegacyQueries,
    private val activityDao: ActivityLegacyQueries,
    private val hba1cDao: HbA1cLegacyQueries,
    private val weightDao: WeightLegacyQueries,
    private val pulseDao: PulseLegacyQueries,
    private val bloodPressureDao: BloodPressureLegacyQueries,
    private val oxygenSaturationDao: OxygenSaturationLegacyQueries,
) : LegacyQueries<MeasurementValue.Legacy> {

    override fun getAll(): List<MeasurementValue.Legacy> {
        return listOf(
            bloodSugarDao,
            insulinDao,
            mealDao,
            activityDao,
            hba1cDao,
            weightDao,
            pulseDao,
            bloodPressureDao,
            oxygenSaturationDao,
        ).flatMap(LegacyQueries<MeasurementValue.Legacy>::getAll)
    }
}