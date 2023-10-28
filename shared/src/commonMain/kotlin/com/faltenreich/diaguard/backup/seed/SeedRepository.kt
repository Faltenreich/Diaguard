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
import com.faltenreich.diaguard.backup.seed.data.WeightSeed

class SeedRepository {

    fun seedMeasurementProperties(): List<Seed<SeedMeasurementProperty>> {
        return listOf(
            BloodSugarSeed(),
            InsulinSeed(),
            MealSeed(),
            ActivitySeed(),
            HbA1cSeed(),
            WeightSeed(),
            PulseSeed(),
            BloodPressureSeed(),
            OxygenSaturationSeed(),
        )
    }

    fun seedFood(): Seed<List<SeedFood>> {
        return FoodSeed()
    }
}