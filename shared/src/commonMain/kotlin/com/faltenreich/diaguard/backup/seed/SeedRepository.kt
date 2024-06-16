package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.backup.seed.query.FoodSeed
import com.faltenreich.diaguard.backup.seed.query.TagSeed
import com.faltenreich.diaguard.backup.seed.query.measurement.ActivitySeed
import com.faltenreich.diaguard.backup.seed.query.measurement.BloodPressureSeed
import com.faltenreich.diaguard.backup.seed.query.measurement.BloodSugarSeed
import com.faltenreich.diaguard.backup.seed.query.measurement.HbA1cSeed
import com.faltenreich.diaguard.backup.seed.query.measurement.InsulinSeed
import com.faltenreich.diaguard.backup.seed.query.measurement.MealSeed
import com.faltenreich.diaguard.backup.seed.query.measurement.OxygenSaturationSeed
import com.faltenreich.diaguard.backup.seed.query.measurement.PulseSeed
import com.faltenreich.diaguard.backup.seed.query.measurement.WeightSeed
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.tag.Tag

class SeedRepository(
    private val bloodSugarSeed: BloodSugarSeed,
    private val insulinSeed: InsulinSeed,
    private val mealSeed: MealSeed,
    private val activitySeed: ActivitySeed,
    private val hbA1cSeed: HbA1cSeed,
    private val weightSeed: WeightSeed,
    private val pulseSeed: PulseSeed,
    private val bloodPressureSeed: BloodPressureSeed,
    private val oxygenSaturationSeed: OxygenSaturationSeed,
    private val foodSeed: FoodSeed,
    private val tagSeed: TagSeed,
) {

    fun getMeasurementCategories(): List<MeasurementCategory.Seed> {
        return listOf(
            bloodSugarSeed(),
            insulinSeed(),
            mealSeed(),
            activitySeed(),
            hbA1cSeed(),
            weightSeed(),
            pulseSeed(),
            bloodPressureSeed(),
            oxygenSaturationSeed(),
        )
    }

    fun getFood(): List<Food.Seed> {
        return foodSeed()
    }

    fun getTags(): List<Tag.Seed> {
        return tagSeed()
    }
}