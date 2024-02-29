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
import com.faltenreich.diaguard.shared.file.SystemFileReader
import com.faltenreich.diaguard.shared.serialization.Serialization
import kotlin.test.Test
import kotlin.test.assertEquals

class SeedRepositoryTest {

    private val seedRepository = SeedRepository(
        bloodSugarSeed = BloodSugarSeed(),
        insulinSeed = InsulinSeed(),
        mealSeed = MealSeed(),
        activitySeed = ActivitySeed(),
        hbA1cSeed = HbA1cSeed(),
        weightSeed = WeightSeed(),
        pulseSeed = PulseSeed(),
        bloodPressureSeed = BloodPressureSeed(),
        oxygenSaturationSeed = OxygenSaturationSeed(),
        foodSeed = FoodSeed(
            fileReader = SystemFileReader("src/commonTest/resources/food.csv"),
            serialization = Serialization(),
        ),
        tagSeed = TagSeed(
            fileReader = SystemFileReader("src/commonTest/resources/tags.csv"),
            serialization = Serialization(),
        ),
    )

    @Test
    fun `Seeded properties have unique keys`() {
        val properties = seedRepository.getMeasurementProperties()
        assertEquals(
            expected = properties,
            actual = properties.distinctBy(SeedMeasurementProperty::key),
        )
    }
}