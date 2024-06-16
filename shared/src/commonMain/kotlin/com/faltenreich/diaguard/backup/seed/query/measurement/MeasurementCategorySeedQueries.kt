package com.faltenreich.diaguard.backup.seed.query.measurement

import com.faltenreich.diaguard.backup.seed.query.SeedQueries
import com.faltenreich.diaguard.measurement.category.MeasurementCategory

class MeasurementCategorySeedQueries(
    private val bloodSugarQueries: BloodSugarSeedQueries,
    private val insulinQueries: InsulinSeedQueries,
    private val mealQueries: MealSeedQueries,
    private val activityQueries: ActivitySeedQueries,
    private val hbA1cQueries: HbA1cSeedQueries,
    private val weightQueries: WeightSeedQueries,
    private val pulseQueries: PulseSeedQueries,
    private val bloodPressureQueries: BloodPressureSeedQueries,
    private val oxygenSaturationQueries: OxygenSaturationSeedQueries,
) : SeedQueries<MeasurementCategory.Seed> {

    override fun getAll(): List<MeasurementCategory.Seed> {
        return listOf(
            bloodSugarQueries(),
            insulinQueries(),
            mealQueries(),
            activityQueries(),
            hbA1cQueries(),
            weightQueries(),
            pulseQueries(),
            bloodPressureQueries(),
            oxygenSaturationQueries(),
        )
    }
}