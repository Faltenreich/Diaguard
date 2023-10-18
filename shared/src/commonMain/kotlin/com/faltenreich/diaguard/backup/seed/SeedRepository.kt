package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.backup.seed.property.ActivitySeed
import com.faltenreich.diaguard.backup.seed.property.BloodPressureSeed
import com.faltenreich.diaguard.backup.seed.property.BloodSugarSeed
import com.faltenreich.diaguard.backup.seed.property.HbA1cSeed
import com.faltenreich.diaguard.backup.seed.property.InsulinSeed
import com.faltenreich.diaguard.backup.seed.property.MealSeed
import com.faltenreich.diaguard.backup.seed.property.OxygenSaturationSeed
import com.faltenreich.diaguard.backup.seed.property.PulseSeed
import com.faltenreich.diaguard.backup.seed.property.WeightSeed

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
}