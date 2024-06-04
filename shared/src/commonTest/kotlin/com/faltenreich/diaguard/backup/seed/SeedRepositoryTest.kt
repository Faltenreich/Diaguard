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
import kotlin.test.assertTrue

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
            fileReader = SystemFileReader("src/commonTest/resources/seed/food.csv"),
            serialization = Serialization(),
        ),
        tagSeed = TagSeed(
            fileReader = SystemFileReader("src/commonTest/resources/seed/tags.csv"),
            serialization = Serialization(),
        ),
    )

    @Test
    fun `Seed categories can be read`() {
        assertTrue(seedRepository.getMeasurementCategories().isNotEmpty())
    }

    @Test
    fun `Seed categories have unique keys`() {
        val categories = seedRepository.getMeasurementCategories()
        assertEquals(
            expected = categories,
            actual = categories.distinctBy(SeedMeasurementCategory::key),
        )
    }

    @Test
    fun `Seed food can be read`() {
        assertTrue(seedRepository.getFood().isNotEmpty())
    }

    @Test
    fun `Seed tags can be read`() {
        assertTrue(seedRepository.getTags().isNotEmpty())
    }
}