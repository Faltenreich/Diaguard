package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.backup.seed.data.ActivitySeed
import com.faltenreich.diaguard.backup.seed.data.BloodPressureSeed
import com.faltenreich.diaguard.backup.seed.data.BloodSugarSeed
import com.faltenreich.diaguard.backup.seed.data.FoodSeed
import com.faltenreich.diaguard.backup.seed.data.HbA1cSeed
import com.faltenreich.diaguard.backup.seed.data.InsulinSeed
import com.faltenreich.diaguard.backup.seed.data.MealSeed
import com.faltenreich.diaguard.backup.seed.data.OxygenSaturationSeed
import com.faltenreich.diaguard.backup.seed.data.PulseSeed
import com.faltenreich.diaguard.backup.seed.data.TagSeed
import com.faltenreich.diaguard.backup.seed.data.WeightSeed
import com.faltenreich.diaguard.food.Food
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

    fun getMeasurementCategories(): List<SeedMeasurementCategory> {
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